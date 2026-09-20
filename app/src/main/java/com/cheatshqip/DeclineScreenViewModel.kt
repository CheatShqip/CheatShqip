package com.cheatshqip

import android.database.SQLException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheatshqip.application.port.input.AlbanianWordDetailResult
import com.cheatshqip.application.port.input.GetAlbanianWordDetailUseCase
import com.cheatshqip.domain.Word
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class DeclineScreenViewModel(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val getAlbanianWordDetailUseCase: GetAlbanianWordDetailUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<DeclineScreenUIState>(DeclineScreenUIState.Initial())
    val state = _state.stateIn(
        scope = viewModelScope,
        initialValue = DeclineScreenUIState.Initial(),
        started = SharingStarted.WhileSubscribed(
            stopTimeoutMillis = 1.seconds.inWholeMilliseconds,
            replayExpirationMillis = 9.seconds.inWholeMilliseconds,
        ),
    )

    private var searchJob: Job? = null

    fun onSearch() {
        val search = _state.value.search
        if (search.isBlank()) {
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch(coroutineDispatcher) {
            _state.update { DeclineScreenUIState.Loading(search) }

            try {
                when (val result = getAlbanianWordDetailUseCase.getAlbanianWordDetail(Word(search))) {
                    is AlbanianWordDetailResult.Found -> _state.update { current ->
                        if (current.search != search) {
                            current
                        } else {
                            DeclineScreenUIState.Loaded(search = search, wordDetail = result.wordDetail)
                        }
                    }
                    is AlbanianWordDetailResult.NotFound -> _state.update { current ->
                        if (current.search != search) current else DeclineScreenUIState.NotFound(search = search)
                    }
                }
            } catch (e: SQLException) {
                _state.update { current ->
                    if (current.search != search) current else DeclineScreenUIState.Error(search = search, cause = e)
                }
            }
        }
    }

    fun onSearchChanged(search: String) {
        _state.update { it.onSearchChanged(search) }
    }
}
