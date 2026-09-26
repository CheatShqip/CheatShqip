package com.cheatshqip.appupdate.application.port.output

sealed interface InstallUpdateDeliveryResult {
    data object Started : InstallUpdateDeliveryResult
    data object Failed : InstallUpdateDeliveryResult
}
