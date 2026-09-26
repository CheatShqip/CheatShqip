package com.cheatshqip.appupdate.application

import com.cheatshqip.appupdate.application.port.input.InstallUpdateResult
import com.cheatshqip.appupdate.application.port.input.InstallUpdateUseCase
import com.cheatshqip.appupdate.application.port.output.InstallUpdateDeliveryPort
import com.cheatshqip.appupdate.application.port.output.InstallUpdateDeliveryResult

class InstallUpdateService(
    private val installUpdateDeliveryPort: InstallUpdateDeliveryPort,
) : InstallUpdateUseCase {
    override suspend fun installUpdate(): InstallUpdateResult =
        when (installUpdateDeliveryPort.installUpdateDelivery()) {
            InstallUpdateDeliveryResult.Started -> InstallUpdateResult.Installing
            InstallUpdateDeliveryResult.Failed -> InstallUpdateResult.Failed
        }
}
