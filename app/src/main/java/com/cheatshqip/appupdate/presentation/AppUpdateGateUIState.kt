package com.cheatshqip.appupdate.presentation

import com.cheatshqip.appupdate.domain.VersionCode

sealed interface AppUpdateGateUIState {
    data object Hidden : AppUpdateGateUIState
    data class BlockingUpdate(val availableVersionCode: VersionCode) : AppUpdateGateUIState
    data class BlockingUpdateNotDeliverable(val availableVersionCode: VersionCode) : AppUpdateGateUIState
    data class IncentivePrompt(val availableVersionCode: VersionCode) : AppUpdateGateUIState
    data class IncentiveDownloading(val availableVersionCode: VersionCode) : AppUpdateGateUIState
    data class ReadyToInstall(val availableVersionCode: VersionCode) : AppUpdateGateUIState
}
