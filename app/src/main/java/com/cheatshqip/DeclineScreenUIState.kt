package com.cheatshqip

import androidx.compose.runtime.Immutable
import com.cheatshqip.domain.AlbanianWordDetail

@Immutable
sealed class DeclineScreenUIState {
    abstract val search: String
    abstract fun onSearchChanged(search: String): DeclineScreenUIState

    data class Initial(override val search: String = "") : DeclineScreenUIState() {
        override fun onSearchChanged(search: String): DeclineScreenUIState = this.copy(search = search)
    }

    data class Loading(override val search: String) : DeclineScreenUIState() {
        override fun onSearchChanged(search: String): DeclineScreenUIState = this.copy(search = search)
    }

    data class Loaded(
        override val search: String,
        val wordDetail: AlbanianWordDetail,
    ) : DeclineScreenUIState() {
        override fun onSearchChanged(search: String): DeclineScreenUIState = this.copy(search = search)
    }

    data class NotFound(override val search: String) : DeclineScreenUIState() {
        override fun onSearchChanged(search: String): DeclineScreenUIState = this.copy(search = search)
    }

    data class Error(override val search: String, val cause: Throwable) : DeclineScreenUIState() {
        override fun onSearchChanged(search: String): DeclineScreenUIState = this.copy(search = search)
    }
}
