package com.cheatshqip

import com.cheatshqip.application.TranslationService
import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetWordTranslationSuggestionsUseCaseTest {

    @Test
    fun `given some characters, should propose translations`() {
        val useCase: GetWordTranslationSuggestionsUseCase = TranslationService()

        val result = useCase.getWorldTranslationSuggestions("work")

        assertEquals(
            listOf("punë", "pufe", "pure", "arne", "buçe"),
            result
        )
    }
}