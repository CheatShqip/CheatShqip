package com.cheatshqip

import com.cheatshqip.application.AlbanianWordService
import com.cheatshqip.application.port.input.GetAlbanianWordDetailUseCase
import com.cheatshqip.application.port.output.GetAlbanianWordDetailPort
import com.cheatshqip.domain.Word
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetAlbanianWordDetailUseCaseTest {
    private val expected = albanianWordDetail()
    private val fakePort = GetAlbanianWordDetailPort { expected }
    private val useCase: GetAlbanianWordDetailUseCase = AlbanianWordService(fakePort)

    @Test
    fun `should get albanian word singular definite declensions`() =
        runTest {
            val result = useCase.getAlbanianWordDetail(Word("dhuratë"))

            assertEquals(expected, result)
        }
}
