package com.cheatshqip

import com.cheatshqip.application.port.output.GetEnglishToAlbanianTranslationsPort
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word

class FakeEnglishToAlbanianOutputAdapter : GetEnglishToAlbanianTranslationsPort {
    override suspend fun getTranslationsForEnglishWord(englishWord: Word): List<Translation> {
        return when (englishWord.value) {
            "work" -> listOf(
                Translation("punë"),
                Translation("pufe"),
                Translation("pure"),
                Translation("arne"),
                Translation("buçe")
            )
            "gift" -> listOf(
                Translation("dhuratë"),
                Translation("dhurëti")
            )
            "card" -> listOf(
                Translation("kartë")
            )
            else -> emptyList()
        }
    }
}

class FakeEnglishToAlbanianOutputAdapterWithManyResults : GetEnglishToAlbanianTranslationsPort {
    override suspend fun getTranslationsForEnglishWord(englishWord: Word): List<Translation> {
        return listOf(
            Translation("punë"),
            Translation("pufe"),
            Translation("pure"),
            Translation("arne"),
            Translation("buçe"),
            Translation("puf"),
            Translation("pufi"),
            Translation("pufni"),
            Translation("pufu"),
            Translation("pufur"),
            Translation("pufut"),
        )
    }
}
