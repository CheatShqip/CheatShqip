package com.cheatshqip.adapter.output

import com.cheatshqip.application.port.output.GetWordSuggestionsPort
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word

class RESTWordSuggestionsOutputAdapter(
    private val shqipRESTService: ShqipRESTService
) : GetWordSuggestionsPort {
    override suspend fun getWordSuggestionsOf(word: Word): List<Translation> {
        return shqipRESTService
            .define(word.value)
            .toTranslations()
    }
}
