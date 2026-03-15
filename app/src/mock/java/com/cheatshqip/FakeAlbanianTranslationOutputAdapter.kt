package com.cheatshqip

import com.cheatshqip.application.port.output.GetAlbanianTranslationOfEnglishWordPort
import com.cheatshqip.domain.Word

class FakeAlbanianTranslationOutputAdapter : GetAlbanianTranslationOfEnglishWordPort {
    override suspend fun getAlbanianTranslationOfEnglishWord(englishWord: Word): Word {
        if (englishWord.value == "card") {
            return Word("karte")
        }

        throw IllegalArgumentException("No translation found for ${englishWord.value}")
    }
}
