package com.cheatshqip.appupdate

import com.cheatshqip.appupdate.application.StartUpdateService
import com.cheatshqip.appupdate.application.port.input.StartUpdateResult
import com.cheatshqip.appupdate.application.port.input.StartUpdateUseCase
import com.cheatshqip.appupdate.application.port.output.StartUpdateDeliveryResult
import com.cheatshqip.appupdate.domain.UpdateKind
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StartUpdateServiceTest {
    private val startUpdateDeliveryPort = FakeStartUpdateDeliveryPort()
    private val useCase: StartUpdateUseCase = StartUpdateService(startUpdateDeliveryPort)

    @Test
    fun `given Started, should return Started`() =
        runTest {
            startUpdateDeliveryPort.result = StartUpdateDeliveryResult.Started

            val result = useCase.startUpdate(UpdateKind.Incentive)

            assertEquals(StartUpdateResult.Started, result)
        }

    @Test
    fun `given Declined, should return Declined`() =
        runTest {
            startUpdateDeliveryPort.result = StartUpdateDeliveryResult.Declined

            val result = useCase.startUpdate(UpdateKind.Incentive)

            assertEquals(StartUpdateResult.Declined, result)
        }

    @Test
    fun `given NotDeliverable, should return NotDeliverable`() =
        runTest {
            startUpdateDeliveryPort.result = StartUpdateDeliveryResult.NotDeliverable

            val result = useCase.startUpdate(UpdateKind.Blocking)

            assertEquals(StartUpdateResult.NotDeliverable, result)
        }

    @Test
    fun `given Failed, should return Failed`() =
        runTest {
            startUpdateDeliveryPort.result = StartUpdateDeliveryResult.Failed

            val result = useCase.startUpdate(UpdateKind.Blocking)

            assertEquals(StartUpdateResult.Failed, result)
        }

    @Test
    fun `given any kind, should forward it to the delivery port`() =
        runTest {
            startUpdateDeliveryPort.result = StartUpdateDeliveryResult.Started

            useCase.startUpdate(UpdateKind.Blocking)

            assertEquals(UpdateKind.Blocking, startUpdateDeliveryPort.lastKind)
        }
}
