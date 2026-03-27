package com.cheatshqip

import androidx.compose.runtime.Immutable
import com.cheatshqip.domain.Translation

interface WithSearch {
    val search: String
    fun onSearchChanged(search: String): HomeScreenUIState
}

@Immutable
sealed class HomeScreenUIState {
    data class Initial(override val search: String = "") : HomeScreenUIState(), WithSearch {
        override fun onSearchChanged(search: String): HomeScreenUIState = this.copy(search = search)
    }

    data object Loading : HomeScreenUIState()
    data class WithTranslationSuggestions(
        val translationSuggestions: List<Translation>,
        override val search: String,
    ) : HomeScreenUIState(), WithSearch {
        override fun onSearchChanged(search: String): HomeScreenUIState = this.copy(search = search)
    }

    data class Error(val name: String, val error: Throwable) : HomeScreenUIState()
}
