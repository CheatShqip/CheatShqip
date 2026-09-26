package com.cheatshqip.appupdate

import com.cheatshqip.appupdate.application.port.output.StartUpdateDeliveryPort
import com.cheatshqip.appupdate.application.port.output.StartUpdateDeliveryResult
import com.cheatshqip.appupdate.domain.UpdateKind

class FakeStartUpdateDeliveryPort : StartUpdateDeliveryPort {
    var result: StartUpdateDeliveryResult = StartUpdateDeliveryResult.Failed
    var lastKind: UpdateKind? = null

    override suspend fun startUpdateDelivery(kind: UpdateKind): StartUpdateDeliveryResult {
        lastKind = kind
        return result
    }
}
