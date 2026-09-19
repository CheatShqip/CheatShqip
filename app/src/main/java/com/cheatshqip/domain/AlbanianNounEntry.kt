package com.cheatshqip.domain

data class AlbanianNounEntry(
    val word: Word,
    val kind: WordKind,
    val gender: WordGender,
    val grammaticalDisplay: GrammaticalDisplay,
    val baseForms: NounBaseForms,
)
