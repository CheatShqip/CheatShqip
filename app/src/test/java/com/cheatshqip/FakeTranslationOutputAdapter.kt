package com.cheatshqip

import com.cheatshqip.application.port.output.GetWordTranslationSuggestionsPort
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word

class FakeTranslationOutputAdapter: GetWordTranslationSuggestionsPort {
    override suspend fun getWorldTranslationSuggestions(word: Word): List<Translation> {
        return listOf("punë", "pufe", "pure", "arne", "buçe")
            .map(::Translation)
    }
}