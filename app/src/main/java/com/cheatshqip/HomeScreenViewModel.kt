package com.cheatshqip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class HomeScreenViewModel(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val getWordTranslationSuggestionsUseCase: GetWordTranslationSuggestionsUseCase,
    private val search: MutableStateFlow<String> = MutableStateFlow(""),
) : ViewModel() {
    private val worldTranslationSuggestions: Flow<List<Translation>> = search.map {
        getWordTranslationSuggestionsUseCase.getWorldTranslationSuggestions(Word(it))
    }

    val state = search.flatMapLatest { searchQuery ->
        if (searchQuery.isBlank()) {
            return@flatMapLatest flowOf(HomeScreenUIState.Initial)
        }

        worldTranslationSuggestions
            .map { suggestions -> HomeScreenUIState.WithTranslationSuggestions(suggestions) }
    }.stateIn(
        scope = viewModelScope,
        initialValue = HomeScreenUIState.Initial,
        started =
            SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 1.seconds.inWholeMilliseconds,
                replayExpirationMillis = 9.seconds.inWholeMilliseconds,
            ),
    )

    fun onSearch(search: String) = viewModelScope.launch(coroutineDispatcher) {
        this@HomeScreenViewModel.search.update {
            search
        }
    }
}
