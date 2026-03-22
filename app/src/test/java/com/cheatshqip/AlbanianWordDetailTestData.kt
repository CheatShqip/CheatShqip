package com.cheatshqip

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

fun albanianWordDetail(
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
