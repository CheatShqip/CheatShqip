package com.cheatshqip.application.port.output

import com.cheatshqip.domain.Word

fun interface GetAlbanianTranslationOfEnglishWordPort {
    suspend fun getAlbanianTranslationOfEnglishWord(englishWord: Word): Word
}
