package com.cheatshqip

import com.cheatshqip.domain.Translation

sealed class HomeScreenUIState {
    data class Init(val search: String = "") : HomeScreenUIState()
    data class Loading(val name: String) : HomeScreenUIState()
    data class Success(val name: List<Translation>) : HomeScreenUIState()
    data class Error(val name: String, val error: Throwable) : HomeScreenUIState()
}