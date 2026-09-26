package com.cheatshqip.appupdate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheatshqip.appupdate.application.port.input.CheckForUpdateResult
import com.cheatshqip.appupdate.application.port.input.CheckForUpdateUseCase
import com.cheatshqip.appupdate.application.port.input.InstallUpdateResult
import com.cheatshqip.appupdate.application.port.input.InstallUpdateUseCase
import com.cheatshqip.appupdate.application.port.input.StartUpdateResult
import com.cheatshqip.appupdate.application.port.input.StartUpdateUseCase
import com.cheatshqip.appupdate.domain.UpdateKind
import com.cheatshqip.appupdate.domain.VersionCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class AppUpdateGateViewModel(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val checkForUpdateUseCase: CheckForUpdateUseCase,
    private val startUpdateUseCase: StartUpdateUseCase,
    private val installUpdateUseCase: InstallUpdateUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<AppUpdateGateUIState>(AppUpdateGateUIState.Hidden)
    val state = _state.stateIn(
        scope = viewModelScope,
        initialValue = AppUpdateGateUIState.Hidden,
        started = SharingStarted.WhileSubscribed(
            stopTimeoutMillis = 1.seconds.inWholeMilliseconds,
            replayExpirationMillis = 9.seconds.inWholeMilliseconds,
        ),
    )

    private var availableVersionCode: VersionCode? = null
    private var incentiveDeclined: Boolean = false
    private var pollJob: Job? = null

    init {
        checkForUpdate()
    }

    fun onForeground() {
        checkForUpdate()
    }

    fun onStartBlockingUpdate() {
        val versionCode = availableVersionCode ?: return
        viewModelScope.launch(coroutineDispatcher) {
            _state.value = when (startUpdateUseCase.startUpdate(UpdateKind.Blocking)) {
                StartUpdateResult.Started, StartUpdateResult.Declined ->
                    AppUpdateGateUIState.BlockingUpdate(versionCode)
                StartUpdateResult.NotDeliverable, StartUpdateResult.Failed ->
                    AppUpdateGateUIState.BlockingUpdateNotDeliverable(versionCode)
            }
        }
    }

    fun onIncentiveAccepted() {
        val versionCode = availableVersionCode ?: return
        viewModelScope.launch(coroutineDispatcher) {
            when (startUpdateUseCase.startUpdate(UpdateKind.Incentive)) {
                StartUpdateResult.Started -> {
                    _state.value = AppUpdateGateUIState.IncentiveDownloading(versionCode)
                    pollUntilReadyToInstall()
                }
                StartUpdateResult.Declined, StartUpdateResult.NotDeliverable, StartUpdateResult.Failed ->
                    _state.value = AppUpdateGateUIState.Hidden
            }
        }
    }

    fun onIncentiveDeclined() {
        incentiveDeclined = true
        _state.value = AppUpdateGateUIState.Hidden
    }

    fun onInstallUpdate() {
        viewModelScope.launch(coroutineDispatcher) {
            when (installUpdateUseCase.installUpdate()) {
                InstallUpdateResult.Installing -> _state.value = AppUpdateGateUIState.Hidden
                InstallUpdateResult.Failed -> Unit
            }
        }
    }

    private fun checkForUpdate() {
        viewModelScope.launch(coroutineDispatcher) {
            applyCheckResult(checkForUpdateUseCase.checkForUpdate())
        }
    }

    private fun applyCheckResult(result: CheckForUpdateResult) {
        if (result !is CheckForUpdateResult.UpdateAvailable) {
            _state.value = AppUpdateGateUIState.Hidden
            return
        }
        availableVersionCode = result.flags.availableVersionCode
        _state.value = when {
            result.flags.readyToInstall -> AppUpdateGateUIState.ReadyToInstall(result.flags.availableVersionCode)
            result.flags.kind == UpdateKind.Blocking ->
                AppUpdateGateUIState.BlockingUpdate(result.flags.availableVersionCode)
            incentiveDeclined -> AppUpdateGateUIState.Hidden
            _state.value is AppUpdateGateUIState.IncentiveDownloading -> _state.value
            else -> AppUpdateGateUIState.IncentivePrompt(result.flags.availableVersionCode)
        }
    }

    private fun pollUntilReadyToInstall() {
        pollJob?.cancel()
        pollJob = viewModelScope.launch(coroutineDispatcher) {
            while (_state.value is AppUpdateGateUIState.IncentiveDownloading) {
                delay(POLL_INTERVAL)
                applyCheckResult(checkForUpdateUseCase.checkForUpdate())
            }
        }
    }

    private companion object {
        val POLL_INTERVAL = 2.seconds
    }
}
