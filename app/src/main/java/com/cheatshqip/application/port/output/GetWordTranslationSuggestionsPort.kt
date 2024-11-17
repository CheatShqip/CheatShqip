package com.cheatshqip.application.port.output

import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word

interface GetWordTranslationSuggestionsPort {
    suspend fun getWorldTranslationSuggestions(word: Word): List<Translation>
}