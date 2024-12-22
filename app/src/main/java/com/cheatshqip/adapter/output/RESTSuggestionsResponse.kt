package com.cheatshqip.adapter.output

import com.cheatshqip.domain.Translation
import kotlinx.serialization.Serializable

@Serializable
data class RESTSuggestionsResponse(
    val searchedWord: String,
    val exactMatches: List<RESTExactMatch>,
    val fuzzyMatches: List<RESTFuzzyMatch>,
    val firstCanonicalWord: String?
) {
    fun toTranslations(): List<Translation> {
        return buildList<String> {
            addAll(exactMatches.map(RESTExactMatch::word))
            addAll(fuzzyMatches.map(RESTFuzzyMatch::word))
        }.map(::Translation)
    }
}