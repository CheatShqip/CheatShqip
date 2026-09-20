package com.cheatshqip

import com.cheatshqip.application.port.output.GetAlbanianWordDetailPort
import com.cheatshqip.domain.AlbanianNounEntry
import com.cheatshqip.domain.GrammaticalDisplay
import com.cheatshqip.domain.NounBaseForms
import com.cheatshqip.domain.PluralGrammaticalDisplay
import com.cheatshqip.domain.SingularGrammaticalDisplay
import com.cheatshqip.domain.Word
import com.cheatshqip.domain.WordGender
import com.cheatshqip.domain.WordKind

class FakeAlbanianWordDetailOutputAdapter : GetAlbanianWordDetailPort {
    override suspend fun getAlbanianWord(word: Word): AlbanianNounEntry =
        wordEntries[word.normalize()] ?: error("No detail found for word: ${word.value}")

    private companion object {
        val wordEntries: Map<Word, AlbanianNounEntry> = mapOf(
            Word("dhurate") to AlbanianNounEntry(
                word = Word("dhuratë"),
                kind = WordKind.Name,
                gender = WordGender.Feminine,
                grammaticalDisplay = GrammaticalDisplay(
                    singular = SingularGrammaticalDisplay("dhurát/ë,-a"),
                    plural = PluralGrammaticalDisplay("dhurát/a,-at"),
                ),
                baseForms = NounBaseForms(
                    singularIndefinite = "dhuratë",
                    singularDefinite = "dhurata",
                    pluralIndefinite = "dhurata",
                    pluralDefinite = "dhuratat",
                ),
            ),
            Word("karte") to AlbanianNounEntry(
                word = Word("kartë"),
                kind = WordKind.Name,
                gender = WordGender.Feminine,
                grammaticalDisplay = GrammaticalDisplay(
                    singular = SingularGrammaticalDisplay("kár/të,-ta"),
                    plural = PluralGrammaticalDisplay("kártat"),
                ),
                baseForms = NounBaseForms(
                    singularIndefinite = "kartë",
                    singularDefinite = "karta",
                    pluralIndefinite = "karta",
                    pluralDefinite = "kartat",
                ),
            ),
        )
    }
}
