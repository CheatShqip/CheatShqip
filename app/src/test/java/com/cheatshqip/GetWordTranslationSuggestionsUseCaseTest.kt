package com.cheatshqip

import com.cheatshqip.application.TranslationService
import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class GetWordTranslationSuggestionsUseCaseTest {

    @MethodSource("provideArguments")
    @ParameterizedTest
    fun `given some characters, should propose translations`(
        word: Word,
        expectedTranslations: List<Translation>
    ) = runTest {
        val useCase: GetWordTranslationSuggestionsUseCase = TranslationService(
            getAlbanianTranslationOfEnglishWordPort = FakeAlbanianTranslationOutputAdapter(),
            getWordSuggestionsPort = FakeWordSuggestionsOutputAdapter(),
        )

        val result = useCase.getWorldTranslationSuggestions(word)

        assertEquals(
            expectedTranslations,
            result
        )
    }

    companion object {
        @Suppress("unused")
        @JvmStatic
        private fun provideArguments() = listOf(
            Arguments.of(
                Word("work"),
                listOf(
                    Translation("punë"),
                    Translation("pufe"),
                    Translation("pure"),
                    Translation("arne"),
                    Translation("buçe")
                )
            ),
            Arguments.of(
                Word("gift"),
                listOf(
                    Translation("dhuratë"),
                    Translation("dhurëti"),
                )
            )
        )
    }
}