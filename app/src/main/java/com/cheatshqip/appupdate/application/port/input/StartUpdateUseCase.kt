package com.cheatshqip.appupdate.application.port.input

import com.cheatshqip.appupdate.domain.UpdateKind

fun interface StartUpdateUseCase {
    suspend fun startUpdate(kind: UpdateKind): StartUpdateResult
}
