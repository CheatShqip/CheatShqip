package com.cheatshqip.application

import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.application.port.output.GetAlbanianTranslationOfEnglishWordPort
import com.cheatshqip.application.port.output.GetWordSuggestionsPort
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word
import kotlin.collections.take

private const val MAX_NUMBER_OF_RESULTS = 5

class TranslationService(
    private val getAlbanianTranslationOfEnglishWordPort: GetAlbanianTranslationOfEnglishWordPort,
    private val getWordSuggestionsPort: GetWordSuggestionsPort,
) : GetWordTranslationSuggestionsUseCase {
    override suspend fun getWorldTranslationSuggestions(englishWord: Word): List<Translation> {
        val albanianTranslation =
            getAlbanianTranslationOfEnglishWordPort
            .getAlbanianTranslationOfEnglishWord(englishWord)

        return getWordSuggestionsPort
            .getWordSuggestionsOf(albanianTranslation.normalize())
            .take(MAX_NUMBER_OF_RESULTS)
    }
}
