package com.cheatshqip.appupdate.application.port.input

fun interface InstallUpdateUseCase {
    suspend fun installUpdate(): InstallUpdateResult
}
