package com.cheatshqip.appupdate

import com.cheatshqip.appupdate.application.port.output.InstallUpdateDeliveryPort
import com.cheatshqip.appupdate.application.port.output.InstallUpdateDeliveryResult

class FakeInstallUpdateDeliveryPort : InstallUpdateDeliveryPort {
    var result: InstallUpdateDeliveryResult = InstallUpdateDeliveryResult.Failed

    override suspend fun installUpdateDelivery(): InstallUpdateDeliveryResult = result
}
