package com.cheatshqip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.domain.Word
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModel(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val getWordTranslationSuggestionsUseCase: GetWordTranslationSuggestionsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<HomeScreenUIState>(HomeScreenUIState.Initial())
    val state = _state.stateIn(
        scope = viewModelScope,
        initialValue = HomeScreenUIState.Initial(),
        started =
            SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 1.seconds.inWholeMilliseconds,
                replayExpirationMillis = 9.seconds.inWholeMilliseconds,
            ),
    )

    fun onSearch() = viewModelScope.launch(coroutineDispatcher) {
        val currentState = _state.value
        check(currentState is WithSearch)

        getWordTranslationSuggestionsUseCase
            .getWorldTranslationSuggestions(Word(currentState.search))
            .let { translations ->
                _state.update {
                    HomeScreenUIState.WithTranslationSuggestions(
                        translationSuggestions = translations,
                        search = currentState.search
                    )
                }
            }
    }

    fun onSearchChanged(search: String) {
        val currentState = _state.value
        check(currentState is WithSearch)

        _state.update { currentState.onSearchChanged(search) }
    }
}
