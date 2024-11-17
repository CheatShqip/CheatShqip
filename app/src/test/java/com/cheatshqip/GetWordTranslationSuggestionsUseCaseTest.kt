package com.cheatshqip

import com.cheatshqip.application.TranslationService
import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetWordTranslationSuggestionsUseCaseTest {

    @Test
    fun `given some characters, should propose translations`() = runTest {
        val useCase: GetWordTranslationSuggestionsUseCase = TranslationService(
            getWordTranslationSuggestionsPort = FakeTranslationOutputAdapter()
        )

        val result = useCase.getWorldTranslationSuggestions(Word("work"))

        assertEquals(
            listOf(
                Translation("punë"),
                Translation("pufe"),
                Translation("pure"),
                Translation("arne"),
                Translation("buçe")
            ),
            result
        )
    }
}