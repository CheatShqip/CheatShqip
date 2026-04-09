package com.cheatshqip

import androidx.compose.runtime.Immutable
import com.cheatshqip.domain.AlbanianWordDetail

@Immutable
sealed class WordDetailUIState {
    data object Loading : WordDetailUIState()
    data class Loaded(val wordDetail: AlbanianWordDetail) : WordDetailUIState()
    data class Error(val cause: Throwable) : WordDetailUIState()
}
