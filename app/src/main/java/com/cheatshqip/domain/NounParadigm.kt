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
            val (sgHead, sgIndefModifier) = splitCompound(baseForms.singularIndefinite)
            val (_, sgDefModifier) = splitCompound(baseForms.singularDefinite)
            val (plHead, plIndefModifier) = splitCompound(baseForms.pluralIndefinite)
            val (_, plDefModifier) = splitCompound(baseForms.pluralDefinite)
            val stem = sgHead.removeSuffix(UNSTRESSED_E_ENDING)
            return NounParadigm(
                singularIndefinite = singularIndefiniteDeclensions(
                    stem = stem,
                    nominative = baseForms.singularIndefinite,
                    modifier = sgIndefModifier,
                ),
                singularDefinite = singularDefiniteDeclensions(
                    stem = stem,
                    nominative = baseForms.singularDefinite,
                    modifier = sgDefModifier,
                ),
                pluralIndefinite = pluralIndefiniteDeclensions(
                    pluralHead = plHead,
                    nominative = baseForms.pluralIndefinite,
                    modifier = plIndefModifier,
                ),
                pluralDefinite = pluralDefiniteDeclensions(
                    pluralHead = plHead,
                    nominative = baseForms.pluralDefinite,
                    modifier = plDefModifier,
                ),
            )
        }

        private fun splitCompound(form: String): Pair<String, String> {
            val spaceIndex = form.indexOf(' ')
            if (spaceIndex < 0) return form to ""
            return form.substring(0, spaceIndex) to form.substring(spaceIndex)
        }

        private fun singularIndefiniteDeclensions(
            stem: String,
            nominative: String,
            modifier: String,
        ): AlbanianDeclensions {
            val oblique = stem + INDEFINITE_OBLIQUE_SUFFIX + modifier
            return AlbanianDeclensions(
                nominative = NominativeDeclension(nominative),
                genitive = GenitiveDeclension(oblique),
                dative = DativeDeclension(oblique),
                accusative = AccusativeDeclension(nominative),
                ablative = AblativeDeclension(oblique),
            )
        }

        private fun singularDefiniteDeclensions(
            stem: String,
            nominative: String,
            modifier: String,
        ): AlbanianDeclensions {
            val oblique = stem + definiteObliqueSuffix(stem) + modifier
            val accusative = stem + definiteAccusativeSuffix(stem) + modifier
            return AlbanianDeclensions(
                nominative = NominativeDeclension(nominative),
                genitive = GenitiveDeclension(oblique),
                dative = DativeDeclension(oblique),
                accusative = AccusativeDeclension(accusative),
                ablative = AblativeDeclension(oblique),
            )
        }

        private fun pluralIndefiniteDeclensions(
            pluralHead: String,
            nominative: String,
            modifier: String,
        ): AlbanianDeclensions {
            val oblique = pluralHead + PLURAL_OBLIQUE_SUFFIX + modifier
            return AlbanianDeclensions(
                nominative = NominativeDeclension(nominative),
                genitive = GenitiveDeclension(oblique),
                dative = DativeDeclension(oblique),
                accusative = AccusativeDeclension(nominative),
                ablative = AblativeDeclension(oblique),
            )
        }

        private fun pluralDefiniteDeclensions(
            pluralHead: String,
            nominative: String,
            modifier: String,
        ): AlbanianDeclensions {
            val oblique = pluralHead + PLURAL_OBLIQUE_SUFFIX + modifier
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
