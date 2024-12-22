package com.cheatshqip

import com.cheatshqip.application.TranslationService
import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.application.port.output.GetWordSuggestionsPort
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
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

    @Test
    fun `given some characters, the maximum number of proposed translations is 5`() = runTest {
        val useCase: GetWordTranslationSuggestionsUseCase = TranslationService(
            getAlbanianTranslationOfEnglishWordPort = FakeAlbanianTranslationOutputAdapter(),
            getWordSuggestionsPort = object : GetWordSuggestionsPort {
                override suspend fun getWordSuggestionsOf(word: Word): List<Translation> {
                    return listOf(
                        Translation("punë"),
                        Translation("pufe"),
                        Translation("pure"),
                        Translation("arne"),
                        Translation("buçe"),
                        Translation("puf"),
                        Translation("pufi"),
                        Translation("pufni"),
                        Translation("pufu"),
                        Translation("pufur"),
                        Translation("pufut"),
                    )
                }

            }
        )

        val result = useCase.getWorldTranslationSuggestions(Word("work"))

        assertEquals(
            listOf(
                Translation("punë"),
                Translation("pufe"),
                Translation("pure"),
                Translation("arne"),
                Translation("buçe"),
            ),
            result
        )
    }

    @Test
    fun `given some characters, they should be normalized to work`() = runTest {
        val useCase: GetWordTranslationSuggestionsUseCase = TranslationService(
            getAlbanianTranslationOfEnglishWordPort = FakeAlbanianTranslationOutputAdapter(),
            getWordSuggestionsPort = object : GetWordSuggestionsPort {
                override suspend fun getWordSuggestionsOf(word: Word): List<Translation> {
                    if (word.value == "pune") {
                        return listOf(
                            Translation("punë"),
                            Translation("pufe"),
                            Translation("pure"),
                            Translation("arne"),
                            Translation("buçe"),
                        )
                    }
                    throw IllegalArgumentException("No translation found for ${word.value}")
                }
            }
        )

        val result = useCase.getWorldTranslationSuggestions(Word("work"))

        assertEquals(
            listOf(
                Translation("punë"),
                Translation("pufe"),
                Translation("pure"),
                Translation("arne"),
                Translation("buçe"),
            ),
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