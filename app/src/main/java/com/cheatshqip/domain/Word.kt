package com.cheatshqip.domain

import java.text.Normalizer

@JvmInline
value class Word(val value: String) {
    fun normalize(): Word {
        val normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
        val withoutDiacritics = normalized.replace(Regex("\\p{Mn}+"), "")

        return Word(withoutDiacritics)
    }
}
