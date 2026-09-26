package com.cheatshqip.appupdate.adapter.output

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.cheatshqip.appupdate.application.port.output.StartUpdateDeliveryPort
import com.cheatshqip.appupdate.application.port.output.StartUpdateDeliveryResult
import com.cheatshqip.appupdate.domain.UpdateKind
import com.google.android.gms.common.api.ApiException
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.google.android.play.core.ktx.requestAppUpdateInfo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import kotlin.coroutines.resume

class PlayStartUpdateDeliveryOutputAdapter(
    private val appUpdateManager: AppUpdateManager,
    private val currentActivityProvider: CurrentActivityProvider,
    private val mapper: PlayUpdateInfoMapper,
) : StartUpdateDeliveryPort {
    override suspend fun startUpdateDelivery(kind: UpdateKind): StartUpdateDeliveryResult {
        val activity = currentActivityProvider.currentActivity
            ?: return StartUpdateDeliveryResult.NotDeliverable
        val appUpdateInfo = try {
            appUpdateManager.requestAppUpdateInfo()
        } catch (e: CancellationException) {
            throw e
        } catch (ignored: ApiException) {
            return StartUpdateDeliveryResult.Failed
        }
        if (!isUpdateAllowed(appUpdateInfo, kind)) return StartUpdateDeliveryResult.NotDeliverable
        return startUpdateFlow(activity, appUpdateInfo, kind)
    }

    private fun isUpdateAllowed(appUpdateInfo: AppUpdateInfo, kind: UpdateKind): Boolean =
        when (kind) {
            UpdateKind.Blocking -> appUpdateInfo.isImmediateUpdateAllowed
            UpdateKind.Incentive -> appUpdateInfo.isFlexibleUpdateAllowed
        }

    private suspend fun startUpdateFlow(
        activity: ComponentActivity,
        appUpdateInfo: AppUpdateInfo,
        kind: UpdateKind,
    ): StartUpdateDeliveryResult = suspendCancellableCoroutine { continuation ->
        lateinit var launcher: ActivityResultLauncher<IntentSenderRequest>
        launcher = activity.activityResultRegistry.register(
            "app-update-${UUID.randomUUID()}",
            ActivityResultContracts.StartIntentSenderForResult(),
        ) { result ->
            launcher.unregister()
            continuation.resume(mapper.mapStartResult(result.resultCode))
        }
        continuation.invokeOnCancellation { launcher.unregister() }
        val started = appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            launcher,
            AppUpdateOptions.defaultOptions(mapper.mapKindToUpdateType(kind)),
        )
        if (!started) {
            launcher.unregister()
            continuation.resume(StartUpdateDeliveryResult.NotDeliverable)
        }
    }
}
