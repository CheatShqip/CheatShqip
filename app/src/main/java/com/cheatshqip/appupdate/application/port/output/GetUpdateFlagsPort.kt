package com.cheatshqip.appupdate.application.port.output

fun interface GetUpdateFlagsPort {
    suspend fun getUpdateFlags(): GetUpdateFlagsResult
}
