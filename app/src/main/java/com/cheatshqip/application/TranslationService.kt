package com.cheatshqip.application

import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word

class TranslationService : GetWordTranslationSuggestionsUseCase {
    override suspend fun getWorldTranslationSuggestions(word: Word): List<Translation> {
        TODO("Not yet implemented")
    }
}
