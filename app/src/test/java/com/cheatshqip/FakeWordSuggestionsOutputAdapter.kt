package com.cheatshqip

import com.cheatshqip.application.port.output.GetWordSuggestionsPort
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word

class FakeWordSuggestionsOutputAdapter : GetWordSuggestionsPort {
    override suspend fun getWordSuggestionsOf(word: Word): List<Translation> {
        if (word.value == "pune") {
            return listOf(
                Translation("punë"),
                Translation("pufe"),
                Translation("pure"),
                Translation("arne"),
                Translation("buçe")
            )
        }
        if (word.value == "dhurate") {
            return listOf(
                Translation("dhuratë"),
                Translation("dhurëti"),
            )
        }

        throw IllegalArgumentException("No suggestions found for ${word.value}")
    }
}