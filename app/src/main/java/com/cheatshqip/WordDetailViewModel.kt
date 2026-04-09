package com.cheatshqip

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheatshqip.application.port.input.GetAlbanianWordDetailUseCase
import com.cheatshqip.domain.Word
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class WordDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getAlbanianWordDetailUseCase: GetAlbanianWordDetailUseCase,
    private val coroutineDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _state = MutableStateFlow<WordDetailUIState>(WordDetailUIState.Loading)
    val state = _state.stateIn(
        scope = viewModelScope,
        initialValue = WordDetailUIState.Loading,
        started = SharingStarted.WhileSubscribed(
            stopTimeoutMillis = 1.seconds.inWholeMilliseconds,
            replayExpirationMillis = 9.seconds.inWholeMilliseconds,
        ),
    )

    private val word: String = checkNotNull(savedStateHandle["word"])

    init {
        loadDetail()
    }

    private fun loadDetail() = viewModelScope.launch(coroutineDispatcher) {
        _state.value = runCatching {
            getAlbanianWordDetailUseCase.getAlbanianWordDetail(Word(word))
        }.fold(
            onSuccess = WordDetailUIState::Loaded,
            onFailure = WordDetailUIState::Error,
        )
    }
}
