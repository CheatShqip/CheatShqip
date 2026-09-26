package com.cheatshqip.appupdate.application.port.input

sealed interface StartUpdateResult {
    data object Started : StartUpdateResult
    data object Declined : StartUpdateResult
    data object NotDeliverable : StartUpdateResult
    data object Failed : StartUpdateResult
}
