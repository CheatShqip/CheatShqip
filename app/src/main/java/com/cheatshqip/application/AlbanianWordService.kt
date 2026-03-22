package com.cheatshqip.application

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

class AlbanianWordService : GetAlbanianWordDetailUseCase {
    override suspend fun getAlbanianWordDetail(albanianWord: Word): AlbanianWordDetail {
        // todo : revoir le typage parce qu'un albanianword ne doit pas pouvoir être un englishword
        return AlbanianWordDetail(
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
        )
    }
}
