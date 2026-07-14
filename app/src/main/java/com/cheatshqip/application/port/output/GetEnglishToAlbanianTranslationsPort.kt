package com.cheatshqip.application.port.output

import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word

fun interface GetEnglishToAlbanianTranslationsPort {
    suspend fun getTranslationsForEnglishWord(englishWord: Word): List<Translation>
}
