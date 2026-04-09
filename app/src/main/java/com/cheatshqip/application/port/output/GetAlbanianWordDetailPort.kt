package com.cheatshqip.application.port.output

import com.cheatshqip.domain.AlbanianWordDetail
import com.cheatshqip.domain.Word

fun interface GetAlbanianWordDetailPort {
    suspend fun getAlbanianWordDetail(word: Word): AlbanianWordDetail
}
