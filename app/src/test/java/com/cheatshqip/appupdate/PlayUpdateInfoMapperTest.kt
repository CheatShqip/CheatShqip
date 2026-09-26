package com.cheatshqip.appupdate

import android.app.Activity
import com.cheatshqip.appupdate.adapter.output.PlayUpdateInfoMapper
import com.cheatshqip.appupdate.adapter.output.PlayUpdateKindThresholds
import com.cheatshqip.appupdate.application.port.output.GetUpdateFlagsResult
import com.cheatshqip.appupdate.application.port.output.StartUpdateDeliveryResult
import com.cheatshqip.appupdate.domain.UpdateFlags
import com.cheatshqip.appupdate.domain.UpdateKind
import com.cheatshqip.appupdate.domain.VersionCode
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PlayUpdateInfoMapperTest {
    private val mapper = PlayUpdateInfoMapper()

    @Test
    fun `given priority 0, should return NotAvailable`() {
        val result = mapFlags(updatePriority = 0)

        assertEquals(GetUpdateFlagsResult.NotAvailable, result)
    }

    @Test
    fun `given priority 1, should return Incentive`() {
        val result = mapFlags(updatePriority = 1)

        assertEquals(available(UpdateKind.Incentive), result)
    }

    @Test
    fun `given priority 3, should return Incentive`() {
        val result = mapFlags(updatePriority = 3)

        assertEquals(available(UpdateKind.Incentive), result)
    }

    @Test
    fun `given priority 4, should return Blocking`() {
        val result = mapFlags(updatePriority = 4)

        assertEquals(available(UpdateKind.Blocking), result)
    }

    @Test
    fun `given priority 5, should return Blocking`() {
        val result = mapFlags(updatePriority = 5)

        assertEquals(available(UpdateKind.Blocking), result)
    }

    @Test
    fun `given custom thresholds, should honour them`() {
        val thresholds = PlayUpdateKindThresholds(blockingFromPriority = 2, incentiveFromPriority = 2)

        val result = mapFlags(updatePriority = 2, thresholds = thresholds)

        assertEquals(available(UpdateKind.Blocking), result)
    }

    @Test
    fun `given UPDATE_AVAILABLE, should return Available`() {
        val result = mapFlags(updateAvailability = UpdateAvailability.UPDATE_AVAILABLE)

        assertEquals(available(UpdateKind.Incentive), result)
    }

    @Test
    fun `given DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS, should return Available`() {
        val result = mapFlags(
            updateAvailability = UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS,
        )

        assertEquals(available(UpdateKind.Incentive), result)
    }

    @Test
    fun `given UPDATE_NOT_AVAILABLE, should return NotAvailable`() {
        val result = mapFlags(updateAvailability = UpdateAvailability.UPDATE_NOT_AVAILABLE)

        assertEquals(GetUpdateFlagsResult.NotAvailable, result)
    }

    @Test
    fun `given unknown availability, should return NotAvailable`() {
        val result = mapFlags(updateAvailability = UpdateAvailability.UNKNOWN)

        assertEquals(GetUpdateFlagsResult.NotAvailable, result)
    }

    @Test
    fun `given installStatus DOWNLOADED, should set readyToInstall and force kind Incentive`() {
        val result = mapFlags(
            updatePriority = 5,
            installStatus = InstallStatus.DOWNLOADED,
        )

        assertEquals(
            GetUpdateFlagsResult.Available(
                UpdateFlags(
                    kind = UpdateKind.Incentive,
                    availableVersionCode = VersionCode(AVAILABLE_VERSION_CODE),
                    readyToInstall = true,
                ),
            ),
            result,
        )
    }

    @Test
    fun `given kind Blocking, should map to IMMEDIATE`() {
        val result = mapper.mapKindToUpdateType(UpdateKind.Blocking)

        assertEquals(AppUpdateType.IMMEDIATE, result)
    }

    @Test
    fun `given kind Incentive, should map to FLEXIBLE`() {
        val result = mapper.mapKindToUpdateType(UpdateKind.Incentive)

        assertEquals(AppUpdateType.FLEXIBLE, result)
    }

    @Test
    fun `given result code OK, should map Started`() {
        val result = mapper.mapStartResult(Activity.RESULT_OK)

        assertEquals(StartUpdateDeliveryResult.Started, result)
    }

    @Test
    fun `given result code CANCELED, should map Declined`() {
        val result = mapper.mapStartResult(Activity.RESULT_CANCELED)

        assertEquals(StartUpdateDeliveryResult.Declined, result)
    }

    @Test
    fun `given result code IN_APP_UPDATE_FAILED, should map Failed`() {
        val result = mapper.mapStartResult(ActivityResult.RESULT_IN_APP_UPDATE_FAILED)

        assertEquals(StartUpdateDeliveryResult.Failed, result)
    }

    @Test
    fun `given an unknown result code, should map Failed`() {
        val result = mapper.mapStartResult(42)

        assertEquals(StartUpdateDeliveryResult.Failed, result)
    }

    private fun mapFlags(
        updateAvailability: Int = UpdateAvailability.UPDATE_AVAILABLE,
        updatePriority: Int = 1,
        installStatus: Int = InstallStatus.UNKNOWN,
        thresholds: PlayUpdateKindThresholds = PlayUpdateKindThresholds(),
    ): GetUpdateFlagsResult = mapper.mapFlags(
        updateAvailability = updateAvailability,
        updatePriority = updatePriority,
        availableVersionCode = AVAILABLE_VERSION_CODE,
        installStatus = installStatus,
        thresholds = thresholds,
    )

    private fun available(kind: UpdateKind) = GetUpdateFlagsResult.Available(
        UpdateFlags(
            kind = kind,
            availableVersionCode = VersionCode(AVAILABLE_VERSION_CODE),
            readyToInstall = false,
        ),
    )

    private companion object {
        const val AVAILABLE_VERSION_CODE = 7
    }
}
