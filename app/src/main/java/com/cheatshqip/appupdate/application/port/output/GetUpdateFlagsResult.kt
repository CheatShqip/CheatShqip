package com.cheatshqip.appupdate.application.port.output

import com.cheatshqip.appupdate.domain.UpdateFlags

sealed interface GetUpdateFlagsResult {
    data class Available(val flags: UpdateFlags) : GetUpdateFlagsResult
    data object NotAvailable : GetUpdateFlagsResult
    data object Failed : GetUpdateFlagsResult
}
