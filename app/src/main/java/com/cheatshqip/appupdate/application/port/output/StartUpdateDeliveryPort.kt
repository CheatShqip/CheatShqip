package com.cheatshqip.appupdate.application.port.output

import com.cheatshqip.appupdate.domain.UpdateKind

fun interface StartUpdateDeliveryPort {
    suspend fun startUpdateDelivery(kind: UpdateKind): StartUpdateDeliveryResult
}
