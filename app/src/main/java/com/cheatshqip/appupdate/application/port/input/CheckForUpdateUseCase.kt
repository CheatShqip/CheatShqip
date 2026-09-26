package com.cheatshqip.appupdate.application.port.input

fun interface CheckForUpdateUseCase {
    suspend fun checkForUpdate(): CheckForUpdateResult
}
