package com.cheatshqip.application

import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.application.port.output.GetEnglishToAlbanianTranslationsPort
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word

private const val MAX_NUMBER_OF_RESULTS = 5

class TranslationService(
    private val getEnglishToAlbanianTranslationsPort: GetEnglishToAlbanianTranslationsPort,
) : GetWordTranslationSuggestionsUseCase {
    override suspend fun getWorldTranslationSuggestions(word: Word): List<Translation> {
        return getEnglishToAlbanianTranslationsPort
            .getTranslationsForEnglishWord(word.normalize())
            .take(MAX_NUMBER_OF_RESULTS)
    }
}
