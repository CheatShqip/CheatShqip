package com.cheatshqip

import com.cheatshqip.application.port.output.GetAlbanianTranslationOfEnglishWordPort
import com.cheatshqip.domain.Word

class FakeAlbanianTranslationOutputAdapter : GetAlbanianTranslationOfEnglishWordPort {

    private val translations = mapOf("card" to "karte")

    override suspend fun getAlbanianTranslationOfEnglishWord(englishWord: Word): Word {
        return Word(
            translations[englishWord.value]
                ?: throw IllegalArgumentException("No translation found for ${englishWord.value}")
        )
    }
}
