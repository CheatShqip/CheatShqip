package com.cheatshqip.appupdate.application.port.input

import com.cheatshqip.appupdate.domain.UpdateFlags

sealed interface CheckForUpdateResult {
    data class UpdateAvailable(val flags: UpdateFlags) : CheckForUpdateResult
    data object NoUpdate : CheckForUpdateResult
    data object CheckFailed : CheckForUpdateResult
}
