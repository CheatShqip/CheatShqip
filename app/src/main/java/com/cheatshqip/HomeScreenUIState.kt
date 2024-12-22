package com.cheatshqip

import com.cheatshqip.domain.Translation

sealed class HomeScreenUIState(open val search: String) {
    fun onSearchChanged(search: String): HomeScreenUIState {
        return when (this) {
            is WithSearch -> copy(search = search)
            is WithSearchAndWithTranslationSuggestions -> copy(search = search)
            else -> error("Cannot change search on $this")
        }
    }

    data class WithSearch(override val search: String) : HomeScreenUIState(search)
    data object Loading: HomeScreenUIState("")
    data class WithSearchAndWithTranslationSuggestions(
        override val search: String,
        val translationSuggestions: List<Translation>
    ) : HomeScreenUIState(search)
    data class Error(val name: String, val error: Throwable) : HomeScreenUIState("")
}