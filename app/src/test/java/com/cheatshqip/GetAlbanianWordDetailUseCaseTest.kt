package com.cheatshqip

import com.cheatshqip.application.AlbanianWordService
import com.cheatshqip.application.port.input.AlbanianWordDetailResult
import com.cheatshqip.application.port.input.GetAlbanianWordDetailUseCase
import com.cheatshqip.domain.AblativeDeclension
import com.cheatshqip.domain.AccusativeDeclension
import com.cheatshqip.domain.AlbanianDeclensions
import com.cheatshqip.domain.AlbanianWordDetail
import com.cheatshqip.domain.DativeDeclension
import com.cheatshqip.domain.GenitiveDeclension
import com.cheatshqip.domain.GrammaticalDisplay
import com.cheatshqip.domain.NominativeDeclension
import com.cheatshqip.domain.NounParadigm
import com.cheatshqip.domain.PluralGrammaticalDisplay
import com.cheatshqip.domain.SingularGrammaticalDisplay
import com.cheatshqip.domain.Word
import com.cheatshqip.domain.WordGender
import com.cheatshqip.domain.WordKind
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetAlbanianWordDetailUseCaseTest {
    private val getAlbanianWordDetailPort = FakeAlbanianWordDetailOutputAdapter()
    private val useCase: GetAlbanianWordDetailUseCase = AlbanianWordService(getAlbanianWordDetailPort)

    @Test
    fun `should get albanian word full declension paradigm`() =
        runTest {
            val result = useCase.getAlbanianWordDetail(Word("dhuratë"))

            val expected = AlbanianWordDetailResult.Found(albanianWordDetail())
            assertEquals(expected, result)
        }

    @Test
    fun `should return not found when word has no detail`() =
        runTest {
            val result = useCase.getAlbanianWordDetail(Word("unknown"))

            assertEquals(AlbanianWordDetailResult.NotFound, result)
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
    declensions: NounParadigm = NounParadigm(
        singularIndefinite = declensions(
            nominative = "dhuratë",
            oblique = "dhurate",
            accusative = "dhuratë",
        ),
        singularDefinite = declensions(
            nominative = "dhurata",
            oblique = "dhuratës",
            accusative = "dhuratën",
        ),
        pluralIndefinite = declensions(
            nominative = "dhurata",
            oblique = "dhuratave",
            accusative = "dhurata",
        ),
        pluralDefinite = declensions(
            nominative = "dhuratat",
            oblique = "dhuratave",
            accusative = "dhuratat",
        ),
    ),
): AlbanianWordDetail = AlbanianWordDetail(
    word = word,
    kind = kind,
    gender = gender,
    grammaticalDisplay = grammaticalDisplay,
    declensions = declensions,
)

private fun declensions(
    nominative: String,
    oblique: String,
    accusative: String,
): AlbanianDeclensions = AlbanianDeclensions(
    nominative = NominativeDeclension(nominative),
    genitive = GenitiveDeclension(oblique),
    dative = DativeDeclension(oblique),
    accusative = AccusativeDeclension(accusative),
    ablative = AblativeDeclension(oblique),
)
