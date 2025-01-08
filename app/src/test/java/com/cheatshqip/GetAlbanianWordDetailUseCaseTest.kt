package com.cheatshqip

import com.cheatshqip.application.AlbanianWordService
import com.cheatshqip.application.port.input.GetAlbanianWordDetailUseCase
import com.cheatshqip.domain.AblativeDeclension
import com.cheatshqip.domain.AccusativeDeclension
import com.cheatshqip.domain.AlbanianWordDetail
import com.cheatshqip.domain.DativeDeclension
import com.cheatshqip.domain.AlbanianDeclensions
import com.cheatshqip.domain.GenitiveDeclension
import com.cheatshqip.domain.GrammaticalDisplay
import com.cheatshqip.domain.NominativeDeclension
import com.cheatshqip.domain.PluralGrammaticalDisplay
import com.cheatshqip.domain.SingularGrammaticalDisplay
import com.cheatshqip.domain.Word
import com.cheatshqip.domain.WordGender
import com.cheatshqip.domain.WordKind
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetAlbanianWordDetailUseCaseTest {

    @Test
    fun `should get albanian word singular definite declensions`() = runTest {
        val useCase: GetAlbanianWordDetailUseCase = AlbanianWordService()

        val result = useCase.getAlbanianWordDetail(Word("dhuratë"))

        assertEquals(Word("dhuratë"), result.word)
        assertEquals(WordKind.Name, result.kind)
        assertEquals(
            GrammaticalDisplay(
                singular = SingularGrammaticalDisplay("dhurát/ë,-a"),
                plural = PluralGrammaticalDisplay("dhurát/a,-at"),
            ),
            result.grammaticalDisplay
        )
        assertEquals(WordGender.Feminine, result.gender)
        assertEquals(
            AlbanianDeclensions(
                nominative = NominativeDeclension("dhurata"),
                genitive = GenitiveDeclension("dhurëtës"),
                dative = DativeDeclension("dhuratës"),
                accusative = AccusativeDeclension("dhuratën"),
                ablative = AblativeDeclension("dhuratës"),
            ),
            result.singularDefiniteDeclensions
        )
    }
}