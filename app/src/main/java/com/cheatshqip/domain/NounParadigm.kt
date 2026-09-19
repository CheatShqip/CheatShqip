package com.cheatshqip.domain

private const val VOWELS = "aeëiouy"

data class NounParadigm(
    val singularIndefinite: AlbanianDeclensions,
    val singularDefinite: AlbanianDeclensions,
    val pluralIndefinite: AlbanianDeclensions,
    val pluralDefinite: AlbanianDeclensions,
) {
    companion object {
        private const val UNSTRESSED_E_ENDING = "ë"
        private const val INDEFINITE_OBLIQUE_SUFFIX = "e"
        private const val PLURAL_OBLIQUE_SUFFIX = "ve"
        private const val CONSONANT_STEM_DEFINITE_OBLIQUE_SUFFIX = "ës"
        private const val CONSONANT_STEM_DEFINITE_ACCUSATIVE_SUFFIX = "ën"
        private const val VOWEL_STEM_DEFINITE_OBLIQUE_SUFFIX = "së"
        private const val VOWEL_STEM_DEFINITE_ACCUSATIVE_SUFFIX = "në"

        fun from(baseForms: NounBaseForms): NounParadigm {
            val stem = baseForms.singularIndefinite.removeSuffix(UNSTRESSED_E_ENDING)
            return NounParadigm(
                singularIndefinite = singularIndefiniteDeclensions(stem, baseForms.singularIndefinite),
                singularDefinite = singularDefiniteDeclensions(stem, baseForms.singularDefinite),
                pluralIndefinite = pluralIndefiniteDeclensions(baseForms.pluralIndefinite),
                pluralDefinite = pluralDefiniteDeclensions(baseForms.pluralIndefinite, baseForms.pluralDefinite),
            )
        }

        private fun singularIndefiniteDeclensions(stem: String, nominative: String): AlbanianDeclensions {
            val oblique = stem + INDEFINITE_OBLIQUE_SUFFIX
            return AlbanianDeclensions(
                nominative = NominativeDeclension(nominative),
                genitive = GenitiveDeclension(oblique),
                dative = DativeDeclension(oblique),
                accusative = AccusativeDeclension(nominative),
                ablative = AblativeDeclension(oblique),
            )
        }

        private fun singularDefiniteDeclensions(stem: String, nominative: String): AlbanianDeclensions {
            val oblique = stem + definiteObliqueSuffix(stem)
            val accusative = stem + definiteAccusativeSuffix(stem)
            return AlbanianDeclensions(
                nominative = NominativeDeclension(nominative),
                genitive = GenitiveDeclension(oblique),
                dative = DativeDeclension(oblique),
                accusative = AccusativeDeclension(accusative),
                ablative = AblativeDeclension(oblique),
            )
        }

        private fun pluralIndefiniteDeclensions(nominative: String): AlbanianDeclensions {
            val oblique = nominative + PLURAL_OBLIQUE_SUFFIX
            return AlbanianDeclensions(
                nominative = NominativeDeclension(nominative),
                genitive = GenitiveDeclension(oblique),
                dative = DativeDeclension(oblique),
                accusative = AccusativeDeclension(nominative),
                ablative = AblativeDeclension(oblique),
            )
        }

        private fun pluralDefiniteDeclensions(pluralIndefinite: String, nominative: String): AlbanianDeclensions {
            val oblique = pluralIndefinite + PLURAL_OBLIQUE_SUFFIX
            return AlbanianDeclensions(
                nominative = NominativeDeclension(nominative),
                genitive = GenitiveDeclension(oblique),
                dative = DativeDeclension(oblique),
                accusative = AccusativeDeclension(nominative),
                ablative = AblativeDeclension(oblique),
            )
        }

        private fun definiteObliqueSuffix(stem: String): String = if (stem.endsWithConsonant()) {
            CONSONANT_STEM_DEFINITE_OBLIQUE_SUFFIX
        } else {
            VOWEL_STEM_DEFINITE_OBLIQUE_SUFFIX
        }

        private fun definiteAccusativeSuffix(stem: String): String = if (stem.endsWithConsonant()) {
            CONSONANT_STEM_DEFINITE_ACCUSATIVE_SUFFIX
        } else {
            VOWEL_STEM_DEFINITE_ACCUSATIVE_SUFFIX
        }

        private fun String.endsWithConsonant(): Boolean {
            val lastCharacter = lastOrNull() ?: return false
            return lastCharacter !in VOWELS
        }
    }
}
