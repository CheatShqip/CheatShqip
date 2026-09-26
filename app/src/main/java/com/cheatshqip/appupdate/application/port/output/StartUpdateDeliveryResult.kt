package com.cheatshqip.appupdate.application.port.output

sealed interface StartUpdateDeliveryResult {
    data object Started : StartUpdateDeliveryResult
    data object Declined : StartUpdateDeliveryResult
    data object NotDeliverable : StartUpdateDeliveryResult
    data object Failed : StartUpdateDeliveryResult
}
