package com.cheatshqip.appupdate.presentation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cheatshqip.R
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.button.ToskButton
import com.cheatshqip.tosk.tokens.primitive.ToskSpacing
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppUpdateGate(
    modifier: Modifier = Modifier,
    viewModel: AppUpdateGateViewModel = koinViewModel(),
    content: @Composable () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    RecheckOnForeground(viewModel)

    Box(modifier = modifier.fillMaxSize()) {
        when (state) {
            is AppUpdateGateUIState.BlockingUpdate -> BlockingUpdateScreen(
                onUpdate = viewModel::onStartBlockingUpdate,
            )
            is AppUpdateGateUIState.BlockingUpdateNotDeliverable -> BlockingUpdateNotDeliverableScreen(
                onUpdate = viewModel::onStartBlockingUpdate,
            )
            else -> content()
        }
        if (state is AppUpdateGateUIState.IncentivePrompt) {
            IncentiveUpdateDialog(
                onAccepted = viewModel::onIncentiveAccepted,
                onDeclined = viewModel::onIncentiveDeclined,
            )
        }
        UpdateSnackbar(
            state = state,
            onInstallUpdate = viewModel::onInstallUpdate,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun RecheckOnForeground(viewModel: AppUpdateGateViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.onForeground()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}

@Composable
private fun BlockingUpdateScreen(
    onUpdate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val updateAction = stringResource(R.string.app_update_blocking_action)
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToskTheme.colors.background.secondary)
            .padding(ToskSpacing.S),
        verticalArrangement = Arrangement.spacedBy(ToskSpacing.S, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.app_update_blocking_title),
            color = ToskTheme.colors.text.primary,
        )
        Text(
            text = stringResource(R.string.app_update_blocking_message),
            color = ToskTheme.colors.text.secondary,
        )
        ToskButton(
            modifier = Modifier.fillMaxWidth(),
            contentDescription = updateAction,
            onClick = onUpdate,
        ) {
            Text(updateAction)
        }
    }
}

@Composable
private fun BlockingUpdateNotDeliverableScreen(
    onUpdate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val updateAction = stringResource(R.string.app_update_blocking_action)
    val openPlayStoreAction = stringResource(R.string.app_update_open_play_store)
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ToskTheme.colors.background.secondary)
            .padding(ToskSpacing.S),
        verticalArrangement = Arrangement.spacedBy(ToskSpacing.S, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.app_update_blocking_title),
            color = ToskTheme.colors.text.primary,
        )
        Text(
            text = stringResource(R.string.app_update_not_deliverable_message),
            color = ToskTheme.colors.text.secondary,
        )
        ToskButton(
            modifier = Modifier.fillMaxWidth(),
            contentDescription = updateAction,
            onClick = onUpdate,
        ) {
            Text(updateAction)
        }
        ToskButton(
            modifier = Modifier.fillMaxWidth(),
            contentDescription = openPlayStoreAction,
            onClick = { openPlayStore(context) },
        ) {
            Text(openPlayStoreAction)
        }
    }
}

@Composable
private fun IncentiveUpdateDialog(
    onAccepted: () -> Unit,
    onDeclined: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDeclined,
        title = { Text(stringResource(R.string.app_update_incentive_title)) },
        text = { Text(stringResource(R.string.app_update_incentive_message)) },
        confirmButton = {
            TextButton(onClick = onAccepted) {
                Text(stringResource(R.string.app_update_incentive_accept))
            }
        },
        dismissButton = {
            TextButton(onClick = onDeclined) {
                Text(stringResource(R.string.app_update_incentive_decline))
            }
        },
    )
}

@Composable
private fun UpdateSnackbar(
    state: AppUpdateGateUIState,
    onInstallUpdate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state !is AppUpdateGateUIState.IncentiveDownloading && state !is AppUpdateGateUIState.ReadyToInstall) {
        return
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val downloadingMessage = stringResource(R.string.app_update_downloading)
    val readyMessage = stringResource(R.string.app_update_ready_message)
    val restartAction = stringResource(R.string.app_update_ready_action)

    LaunchedEffect(state) {
        snackbarHostState.currentSnackbarData?.dismiss()
        when (state) {
            is AppUpdateGateUIState.IncentiveDownloading -> snackbarHostState.showSnackbar(
                message = downloadingMessage,
                duration = SnackbarDuration.Indefinite,
            )
            is AppUpdateGateUIState.ReadyToInstall -> {
                val outcome = snackbarHostState.showSnackbar(
                    message = readyMessage,
                    actionLabel = restartAction,
                    duration = SnackbarDuration.Indefinite,
                )
                if (outcome == SnackbarResult.ActionPerformed) onInstallUpdate()
            }
        }
    }
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier.padding(ToskSpacing.S),
    )
}

private fun openPlayStore(context: Context) {
    val marketIntent = Intent(
        Intent.ACTION_VIEW,
        "market://details?id=${context.packageName}".toUri(),
    ).setPackage("com.android.vending")
    try {
        context.startActivity(marketIntent)
    } catch (ignored: ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/details?id=${context.packageName}".toUri(),
            ),
        )
    }
}

@PreviewLightDark
@Composable
fun BlockingUpdateScreenPreview() {
    ToskTheme {
        BlockingUpdateScreen(onUpdate = {})
    }
}

@PreviewLightDark
@Composable
fun BlockingUpdateNotDeliverableScreenPreview() {
    ToskTheme {
        BlockingUpdateNotDeliverableScreen(onUpdate = {})
    }
}

@PreviewLightDark
@Composable
fun IncentiveUpdateDialogPreview() {
    ToskTheme {
        IncentiveUpdateDialog(onAccepted = {}, onDeclined = {})
    }
}
