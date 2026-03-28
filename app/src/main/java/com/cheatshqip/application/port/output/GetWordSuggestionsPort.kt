package com.cheatshqip.application.port.output

import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word

fun interface GetWordSuggestionsPort {
    suspend fun getWordSuggestionsOf(word: Word): List<Translation>
}
