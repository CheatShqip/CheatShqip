package com.cheatshqip

import com.cheatshqip.application.port.output.GetAlbanianTranslationOfEnglishWordPort
import com.cheatshqip.domain.Word

class FakeAlbanianTranslationOutputAdapter : GetAlbanianTranslationOfEnglishWordPort {
    override suspend fun getAlbanianTranslationOfEnglishWord(englishWord: Word): Word {
        if (englishWord.value == "work") {
            return Word("punë")
        }
        if (englishWord.value == "gift") {
            return Word("dhuratë")
        }

        throw IllegalArgumentException("No translation found for ${englishWord.value}")
    }
}
