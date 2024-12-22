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
    val homeScreenUIState = mutableStateOf<HomeScreenUIState>(HomeScreenUIState.WithSearch(""))

    fun onSearchChanged(search: String) {
        homeScreenUIState.value = homeScreenUIState.value.onSearchChanged(search)
    }

    fun onSearch() = viewModelScope.launch(coroutineDispatcher) {
        val search = homeScreenUIState.value.search

        getWordTranslationSuggestionsUseCase
            .getWorldTranslationSuggestions(Word(search))
            .let { translations ->
                homeScreenUIState.value = HomeScreenUIState.WithSearchAndWithTranslationSuggestions(
                    search,
                    translations
                )
            }
    }
}