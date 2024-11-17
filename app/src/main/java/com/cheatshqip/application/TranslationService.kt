package com.cheatshqip.application

import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.application.port.output.GetWordTranslationSuggestionsPort
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word

class TranslationService(
    private val getWordTranslationSuggestionsPort: GetWordTranslationSuggestionsPort
) : GetWordTranslationSuggestionsUseCase {
    override suspend fun getWorldTranslationSuggestions(word: Word): List<Translation> {
        return getWordTranslationSuggestionsPort.getWorldTranslationSuggestions(word)
    }
}
