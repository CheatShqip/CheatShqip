package com.cheatshqip.adapter.output

import com.cheatshqip.application.port.output.GetWordTranslationSuggestionsPort
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word

class RESTTranslationOutputAdapter(
    private val shqipRESTService: ShqipRESTService
) : GetWordTranslationSuggestionsPort {
    override suspend fun getWorldTranslationSuggestions(word: Word): List<Translation> {
        return shqipRESTService
            .define(word.value)
            .toTranslations()
    }
}