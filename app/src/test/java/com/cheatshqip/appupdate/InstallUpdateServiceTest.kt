package com.cheatshqip.appupdate

import com.cheatshqip.appupdate.application.InstallUpdateService
import com.cheatshqip.appupdate.application.port.input.InstallUpdateResult
import com.cheatshqip.appupdate.application.port.input.InstallUpdateUseCase
import com.cheatshqip.appupdate.application.port.output.InstallUpdateDeliveryResult
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InstallUpdateServiceTest {
    private val installUpdateDeliveryPort = FakeInstallUpdateDeliveryPort()
    private val useCase: InstallUpdateUseCase = InstallUpdateService(installUpdateDeliveryPort)

    @Test
    fun `given Started, should return Installing`() =
        runTest {
            installUpdateDeliveryPort.result = InstallUpdateDeliveryResult.Started

            val result = useCase.installUpdate()

            assertEquals(InstallUpdateResult.Installing, result)
        }

    @Test
    fun `given Failed, should return Failed`() =
        runTest {
            installUpdateDeliveryPort.result = InstallUpdateDeliveryResult.Failed

            val result = useCase.installUpdate()

            assertEquals(InstallUpdateResult.Failed, result)
        }
}
