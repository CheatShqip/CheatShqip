package com.cheatshqip

import androidx.compose.runtime.Immutable
import com.cheatshqip.domain.Translation

@Immutable
sealed class HomeScreenUIState {
    data object Initial : HomeScreenUIState()
    data object Loading : HomeScreenUIState()
    data class WithTranslationSuggestions(
        val translationSuggestions: List<Translation>
    ) : HomeScreenUIState()
    data class Error(val name: String, val error: Throwable) : HomeScreenUIState()
}
