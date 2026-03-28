package com.cheatshqip.application.port.input

import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word

fun interface GetWordTranslationSuggestionsUseCase {
    suspend fun getWorldTranslationSuggestions(word: Word): List<Translation>
}
