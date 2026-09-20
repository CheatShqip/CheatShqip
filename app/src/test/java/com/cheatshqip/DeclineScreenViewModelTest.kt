package com.cheatshqip

import com.cheatshqip.application.AlbanianWordService
import com.cheatshqip.domain.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeclineScreenViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: DeclineScreenViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DeclineScreenViewModel(
            getAlbanianWordDetailUseCase = AlbanianWordService(FakeAlbanianWordDetailOutputAdapter()),
            coroutineDispatcher = testDispatcher,
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given a known word, should load its declensions`() =
        runTest(testDispatcher) {
            viewModel.state.launchIn(backgroundScope)

            viewModel.onSearchChanged("karte")
            viewModel.onSearch()

            val state = viewModel.state.value
            assert(state is DeclineScreenUIState.Loaded) { "expected Loaded but was $state" }
            assertEquals(Word("kartë"), (state as DeclineScreenUIState.Loaded).wordDetail.word)
        }

    @Test
    fun `given an unknown word, should report not found`() =
        runTest(testDispatcher) {
            viewModel.state.launchIn(backgroundScope)

            viewModel.onSearchChanged("zzzznotaword")
            viewModel.onSearch()

            assertEquals(DeclineScreenUIState.NotFound("zzzznotaword"), viewModel.state.value)
        }

    @Test
    fun `given a blank search, should not change state`() =
        runTest(testDispatcher) {
            viewModel.state.launchIn(backgroundScope)

            viewModel.onSearch()

            assertEquals(DeclineScreenUIState.Initial(), viewModel.state.value)
        }

    @Test
    fun `given a failed lookup, search text should survive`() =
        runTest(testDispatcher) {
            viewModel.state.launchIn(backgroundScope)

            viewModel.onSearchChanged("zzzznotaword")
            viewModel.onSearch()

            assertEquals("zzzznotaword", viewModel.state.value.search)
        }
}
