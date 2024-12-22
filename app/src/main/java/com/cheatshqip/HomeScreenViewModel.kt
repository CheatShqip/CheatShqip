package com.cheatshqip

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.domain.Word
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val getWordTranslationSuggestionsUseCase: GetWordTranslationSuggestionsUseCase
) : ViewModel() {
    val homeScreenUIState = mutableStateOf<HomeScreenUIState>(HomeScreenUIState.Init())

    fun onSearchChanged(search: String) {
        homeScreenUIState.value = HomeScreenUIState.Init(search)
    }

    fun onSearch() = viewModelScope.launch(coroutineDispatcher) {
        val state = homeScreenUIState.value as HomeScreenUIState.Init

        getWordTranslationSuggestionsUseCase
            .getWorldTranslationSuggestions(Word(state.search))
            .let { translations ->
                homeScreenUIState.value = HomeScreenUIState.Success(translations)
            }
    }
}