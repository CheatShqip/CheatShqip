package com.cheatshqip.appupdate.application

import com.cheatshqip.appupdate.application.port.input.StartUpdateResult
import com.cheatshqip.appupdate.application.port.input.StartUpdateUseCase
import com.cheatshqip.appupdate.application.port.output.StartUpdateDeliveryPort
import com.cheatshqip.appupdate.application.port.output.StartUpdateDeliveryResult
import com.cheatshqip.appupdate.domain.UpdateKind

class StartUpdateService(
    private val startUpdateDeliveryPort: StartUpdateDeliveryPort,
) : StartUpdateUseCase {
    override suspend fun startUpdate(kind: UpdateKind): StartUpdateResult =
        when (startUpdateDeliveryPort.startUpdateDelivery(kind)) {
            StartUpdateDeliveryResult.Started -> StartUpdateResult.Started
            StartUpdateDeliveryResult.Declined -> StartUpdateResult.Declined
            StartUpdateDeliveryResult.NotDeliverable -> StartUpdateResult.NotDeliverable
            StartUpdateDeliveryResult.Failed -> StartUpdateResult.Failed
        }
}
