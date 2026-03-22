package com.cheatshqip

import com.cheatshqip.application.AlbanianWordService
import com.cheatshqip.application.port.input.GetAlbanianWordDetailUseCase
import com.cheatshqip.domain.Word
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetAlbanianWordDetailUseCaseTest {
    @Test
    fun `should get albanian word singular definite declensions`() =
        runTest {
        val useCase: GetAlbanianWordDetailUseCase = AlbanianWordService()

        val result = useCase.getAlbanianWordDetail(Word("dhuratë"))

        val expected = albanianWordDetail()
        assertEquals(expected, result)
    }
}
