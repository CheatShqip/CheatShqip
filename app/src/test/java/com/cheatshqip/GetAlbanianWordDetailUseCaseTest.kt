package com.cheatshqip

import com.cheatshqip.adapter.output.InMemoryAlbanianWordDetailOutputAdapter
import com.cheatshqip.application.AlbanianWordService
import com.cheatshqip.application.port.input.GetAlbanianWordDetailUseCase
import com.cheatshqip.domain.AblativeDeclension
import com.cheatshqip.domain.AccusativeDeclension
import com.cheatshqip.domain.AlbanianDeclensions
import com.cheatshqip.domain.AlbanianWordDetail
import com.cheatshqip.domain.DativeDeclension
import com.cheatshqip.domain.GenitiveDeclension
import com.cheatshqip.domain.GrammaticalDisplay
import com.cheatshqip.domain.NominativeDeclension
import com.cheatshqip.domain.PluralGrammaticalDisplay
import com.cheatshqip.domain.SingularGrammaticalDisplay
import com.cheatshqip.domain.Word
import com.cheatshqip.domain.WordGender
import com.cheatshqip.domain.WordKind
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetAlbanianWordDetailUseCaseTest {
    private val getAlbanianWordDetailPort = InMemoryAlbanianWordDetailOutputAdapter()
    private val useCase: GetAlbanianWordDetailUseCase = AlbanianWordService(getAlbanianWordDetailPort)

    @Test
    fun `should get albanian word singular definite declensions`() =
        runTest {
            val result = useCase.getAlbanianWordDetail(Word("dhuratë"))

            val expected = albanianWordDetail()
            assertEquals(expected, result)
        }

    @Test
    fun `should throw when word has no detail`() {
        val exception = assertThrows<IllegalStateException> {
            runBlocking { useCase.getAlbanianWordDetail(Word("unknown")) }
        }

        assertEquals("No detail found for word: unknown", exception.message)
    }
}

private fun albanianWordDetail(
    word: Word = Word("dhuratë"),
    kind: WordKind = WordKind.Name,
    gender: WordGender = WordGender.Feminine,
    grammaticalDisplay: GrammaticalDisplay = GrammaticalDisplay(
        singular = SingularGrammaticalDisplay("dhurát/ë,-a"),
        plural = PluralGrammaticalDisplay("dhurát/a,-at"),
    ),
    singularDefiniteDeclensions: AlbanianDeclensions = AlbanianDeclensions(
        nominative = NominativeDeclension("dhurata"),
        genitive = GenitiveDeclension("dhurëtës"),
        dative = DativeDeclension("dhuratës"),
        accusative = AccusativeDeclension("dhuratën"),
        ablative = AblativeDeclension("dhuratës"),
    ),
): AlbanianWordDetail = AlbanianWordDetail(
    word = word,
    kind = kind,
    gender = gender,
    grammaticalDisplay = grammaticalDisplay,
    singularDefiniteDeclensions = singularDefiniteDeclensions,
)