package com.cheatshqip.appupdate.adapter.output

import android.app.Activity
import com.cheatshqip.appupdate.application.port.output.GetUpdateFlagsResult
import com.cheatshqip.appupdate.application.port.output.StartUpdateDeliveryResult
import com.cheatshqip.appupdate.domain.UpdateFlags
import com.cheatshqip.appupdate.domain.UpdateKind
import com.cheatshqip.appupdate.domain.VersionCode
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

class PlayUpdateInfoMapper {
    fun mapFlags(
        updateAvailability: Int,
        updatePriority: Int,
        availableVersionCode: Int,
        installStatus: Int,
        thresholds: PlayUpdateKindThresholds,
    ): GetUpdateFlagsResult {
        if (!isUpdateAvailable(updateAvailability)) return GetUpdateFlagsResult.NotAvailable
        if (installStatus == InstallStatus.DOWNLOADED) {
            return GetUpdateFlagsResult.Available(
                UpdateFlags(
                    kind = UpdateKind.Incentive,
                    availableVersionCode = VersionCode(availableVersionCode),
                    readyToInstall = true,
                ),
            )
        }
        return when {
            updatePriority >= thresholds.blockingFromPriority ->
                availableFlags(UpdateKind.Blocking, availableVersionCode)
            updatePriority >= thresholds.incentiveFromPriority ->
                availableFlags(UpdateKind.Incentive, availableVersionCode)
            else -> GetUpdateFlagsResult.NotAvailable
        }
    }

    fun mapKindToUpdateType(kind: UpdateKind): Int =
        when (kind) {
            UpdateKind.Blocking -> AppUpdateType.IMMEDIATE
            UpdateKind.Incentive -> AppUpdateType.FLEXIBLE
        }

    fun mapStartResult(resultCode: Int): StartUpdateDeliveryResult =
        when (resultCode) {
            Activity.RESULT_OK -> StartUpdateDeliveryResult.Started
            Activity.RESULT_CANCELED -> StartUpdateDeliveryResult.Declined
            ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> StartUpdateDeliveryResult.Failed
            else -> StartUpdateDeliveryResult.Failed
        }

    private fun availableFlags(kind: UpdateKind, availableVersionCode: Int) = GetUpdateFlagsResult.Available(
        UpdateFlags(
            kind = kind,
            availableVersionCode = VersionCode(availableVersionCode),
            readyToInstall = false,
        ),
    )

    private fun isUpdateAvailable(updateAvailability: Int): Boolean =
        updateAvailability == UpdateAvailability.UPDATE_AVAILABLE ||
            updateAvailability == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
}
