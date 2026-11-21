package com.cheatshqip.domain

import java.text.Normalizer

/**
 * Search keys used to match an English query against the bundled dictionary.
 *
 * [canonical] keeps word boundaries as single spaces and strips accents, unicode
 * dashes and punctuation. [compact] removes every separator so spellings such as
 * "letter-box", "letter box" and "letterbox" share the same key.
 */
data class EnglishSearchKeys(val canonical: String, val compact: String) {
    companion object {
        private val COMBINING_MARKS = Regex("\\p{Mn}+")
        private val DASHES = Regex("[\\u2010-\\u2015\\u2212]")
        private val APOSTROPHES = Regex("[\\u2018\\u2019\\u02bc]")
        private val NON_SEARCH = Regex("[^a-z0-9' ]")
        private val WHITESPACE = Regex("\\s+")
        private val NON_COMPACT = Regex("[^a-z0-9]")

        fun from(term: String): EnglishSearchKeys {
            val folded = Normalizer.normalize(term, Normalizer.Form.NFKD)
                .replace(COMBINING_MARKS, "")
                .lowercase()
            val normalized = APOSTROPHES.replace(DASHES.replace(folded, " "), "'")
            val canonical = WHITESPACE.replace(NON_SEARCH.replace(normalized, " "), " ").trim()
            return EnglishSearchKeys(canonical, NON_COMPACT.replace(canonical, ""))
        }
    }
}
