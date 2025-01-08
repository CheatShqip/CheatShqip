package com.cheatshqip.domain

data class AlbanianWordDetail private constructor(
    val word: Word,
    val kind: WordKind,
    val gender: WordGender,
    val grammaticalDisplay: GrammaticalDisplay,
    val singularDefiniteDeclensions: AlbanianDeclensions,
) {
    companion object {
        fun create(
           /* word: Word,
            kind: WordKind,
            gender: WordGender,
            grammaticalDisplay: GrammaticalDisplay,*/
        ): AlbanianWordDetail {
            return AlbanianWordDetail(
                word = Word("dhuratë"),
                kind = WordKind.Name,
                grammaticalDisplay = GrammaticalDisplay(
                    singular = SingularGrammaticalDisplay("dhurát/ë,-a"),
                    plural = PluralGrammaticalDisplay("dhurát/a,-at"),
                ),
                gender = WordGender.Feminine,
                singularDefiniteDeclensions = AlbanianDeclensions(
                    nominative = NominativeDeclension("dhurata"),
                    genitive = GenitiveDeclension("dhurëtës"),
                    dative = DativeDeclension("dhuratës"),
                    accusative = AccusativeDeclension("dhuratën"),
                    ablative = AblativeDeclension("dhuratës"),
                )
            )
        }
    }
}
