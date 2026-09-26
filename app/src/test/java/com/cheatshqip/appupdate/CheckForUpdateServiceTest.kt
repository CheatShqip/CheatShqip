package com.cheatshqip.appupdate

import com.cheatshqip.appupdate.application.CheckForUpdateService
import com.cheatshqip.appupdate.application.port.input.CheckForUpdateResult
import com.cheatshqip.appupdate.application.port.input.CheckForUpdateUseCase
import com.cheatshqip.appupdate.application.port.output.GetUpdateFlagsResult
import com.cheatshqip.appupdate.domain.UpdateFlags
import com.cheatshqip.appupdate.domain.UpdateKind
import com.cheatshqip.appupdate.domain.VersionCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CheckForUpdateServiceTest {
    private val getUpdateFlagsPort = FakeGetUpdateFlagsPort()
    private val useCase: CheckForUpdateUseCase = CheckForUpdateService(getUpdateFlagsPort)

    @Test
    fun `given Available flags, should return UpdateAvailable with same flags`() =
        runTest {
            val flags = UpdateFlags(
                kind = UpdateKind.Incentive,
                availableVersionCode = VersionCode(7),
                readyToInstall = false,
            )
            getUpdateFlagsPort.result = GetUpdateFlagsResult.Available(flags)

            val result = useCase.checkForUpdate()

            assertEquals(CheckForUpdateResult.UpdateAvailable(flags), result)
        }

    @Test
    fun `given NotAvailable, should return NoUpdate`() =
        runTest {
            getUpdateFlagsPort.result = GetUpdateFlagsResult.NotAvailable

            val result = useCase.checkForUpdate()

            assertEquals(CheckForUpdateResult.NoUpdate, result)
        }

    @Test
    fun `given Failed, should return CheckFailed`() =
        runTest {
            getUpdateFlagsPort.result = GetUpdateFlagsResult.Failed

            val result = useCase.checkForUpdate()

            assertEquals(CheckForUpdateResult.CheckFailed, result)
        }
}
