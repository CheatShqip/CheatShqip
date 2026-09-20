package com.cheatshqip.application.port.output

import com.cheatshqip.domain.Word

fun interface GetAlbanianWordDetailPort {
    suspend fun getAlbanianWord(word: Word): AlbanianNounEntryResult
}
