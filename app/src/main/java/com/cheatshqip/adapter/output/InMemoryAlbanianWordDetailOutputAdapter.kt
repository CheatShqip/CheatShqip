package com.cheatshqip.adapter.output

import com.cheatshqip.application.port.output.GetAlbanianWordDetailPort
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

class InMemoryAlbanianWordDetailOutputAdapter : GetAlbanianWordDetailPort {
    override suspend fun getAlbanianWordDetail(word: Word): AlbanianWordDetail =
        wordDetails[word.normalize()] ?: error("No detail found for word: ${word.value}")

    private companion object {
        val wordDetails: Map<Word, AlbanianWordDetail> = mapOf(
            Word("dhurate") to AlbanianWordDetail(
                word = Word("dhuratë"),
                kind = WordKind.Name,
                gender = WordGender.Feminine,
                grammaticalDisplay = GrammaticalDisplay(
                    singular = SingularGrammaticalDisplay("dhurát/ë,-a"),
                    plural = PluralGrammaticalDisplay("dhurát/a,-at"),
                ),
                singularDefiniteDeclensions = AlbanianDeclensions(
                    nominative = NominativeDeclension("dhurata"),
                    genitive = GenitiveDeclension("dhurëtës"),
                    dative = DativeDeclension("dhuratës"),
                    accusative = AccusativeDeclension("dhuratën"),
                    ablative = AblativeDeclension("dhuratës"),
                ),
            ),
            Word("karte") to AlbanianWordDetail(
                word = Word("kartë"),
                kind = WordKind.Name,
                gender = WordGender.Feminine,
                grammaticalDisplay = GrammaticalDisplay(
                    singular = SingularGrammaticalDisplay("kár/të,-ta"),
                    plural = PluralGrammaticalDisplay("kártat"),
                ),
                singularDefiniteDeclensions = AlbanianDeclensions(
                    nominative = NominativeDeclension("karta"),
                    genitive = GenitiveDeclension("kartës"),
                    dative = DativeDeclension("kartës"),
                    accusative = AccusativeDeclension("kartën"),
                    ablative = AblativeDeclension("kartës"),
                ),
            ),
        )
    }
}
