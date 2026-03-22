package com.cheatshqip.domain

data class AlbanianWordDetail(
    val word: Word,
    val kind: WordKind,
    val gender: WordGender,
    val grammaticalDisplay: GrammaticalDisplay,
    val singularDefiniteDeclensions: AlbanianDeclensions,
)
