package com.cheatshqip.appupdate.application.port.output

fun interface InstallUpdateDeliveryPort {
    suspend fun installUpdateDelivery(): InstallUpdateDeliveryResult
}
