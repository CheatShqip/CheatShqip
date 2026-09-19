package com.cheatshqip

import com.cheatshqip.domain.AblativeDeclension
import com.cheatshqip.domain.AccusativeDeclension
import com.cheatshqip.domain.AlbanianDeclensions
import com.cheatshqip.domain.DativeDeclension
import com.cheatshqip.domain.GenitiveDeclension
import com.cheatshqip.domain.NominativeDeclension
import com.cheatshqip.domain.NounBaseForms
import com.cheatshqip.domain.NounParadigm
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class NounParadigmTest {
    @ParameterizedTest
    @MethodSource("provideConsonantStemArguments")
    fun `should derive declensions for a consonant-final stem`(
        baseForms: NounBaseForms,
        expected: NounParadigm,
    ) {
        val result = NounParadigm.from(baseForms)

        assertEquals(expected, result)
    }

    @ParameterizedTest
    @MethodSource("provideVowelStemArguments")
    fun `should derive declensions for a vowel-final stem`(
        baseForms: NounBaseForms,
        expected: NounParadigm,
    ) {
        val result = NounParadigm.from(baseForms)

        assertEquals(expected, result)
    }

    private companion object {
        @JvmStatic
        @Suppress("unused")
        fun provideConsonantStemArguments(): Stream<Arguments> = Stream.of(
            Arguments.of(
                NounBaseForms(
                    singularIndefinite = "kartë",
                    singularDefinite = "karta",
                    pluralIndefinite = "karta",
                    pluralDefinite = "kartat",
                ),
                NounParadigm(
                    singularIndefinite = declensions(
                        nominative = "kartë",
                        oblique = "karte",
                        accusative = "kartë",
                    ),
                    singularDefinite = declensions(
                        nominative = "karta",
                        oblique = "kartës",
                        accusative = "kartën",
                    ),
                    pluralIndefinite = declensions(
                        nominative = "karta",
                        oblique = "kartave",
                        accusative = "karta",
                    ),
                    pluralDefinite = declensions(
                        nominative = "kartat",
                        oblique = "kartave",
                        accusative = "kartat",
                    ),
                ),
            ),
            Arguments.of(
                NounBaseForms(
                    singularIndefinite = "dhuratë",
                    singularDefinite = "dhurata",
                    pluralIndefinite = "dhurata",
                    pluralDefinite = "dhuratat",
                ),
                NounParadigm(
                    singularIndefinite = declensions(
                        nominative = "dhuratë",
                        oblique = "dhurate",
                        accusative = "dhuratë",
                    ),
                    singularDefinite = declensions(
                        nominative = "dhurata",
                        oblique = "dhuratës",
                        accusative = "dhuratën",
                    ),
                    pluralIndefinite = declensions(
                        nominative = "dhurata",
                        oblique = "dhuratave",
                        accusative = "dhurata",
                    ),
                    pluralDefinite = declensions(
                        nominative = "dhuratat",
                        oblique = "dhuratave",
                        accusative = "dhuratat",
                    ),
                ),
            ),
        )

        @JvmStatic
        @Suppress("unused")
        fun provideVowelStemArguments(): Stream<Arguments> = Stream.of(
            Arguments.of(
                NounBaseForms(
                    singularIndefinite = "shtëpi",
                    singularDefinite = "shtëpia",
                    pluralIndefinite = "shtëpi",
                    pluralDefinite = "shtëpitë",
                ),
                NounParadigm(
                    singularIndefinite = declensions(
                        nominative = "shtëpi",
                        oblique = "shtëpie",
                        accusative = "shtëpi",
                    ),
                    singularDefinite = declensions(
                        nominative = "shtëpia",
                        oblique = "shtëpisë",
                        accusative = "shtëpinë",
                    ),
                    pluralIndefinite = declensions(
                        nominative = "shtëpi",
                        oblique = "shtëpive",
                        accusative = "shtëpi",
                    ),
                    pluralDefinite = declensions(
                        nominative = "shtëpitë",
                        oblique = "shtëpive",
                        accusative = "shtëpitë",
                    ),
                ),
            ),
            Arguments.of(
                NounBaseForms(
                    singularIndefinite = "akademi",
                    singularDefinite = "akademia",
                    pluralIndefinite = "akademi",
                    pluralDefinite = "akademitë",
                ),
                NounParadigm(
                    singularIndefinite = declensions(
                        nominative = "akademi",
                        oblique = "akademie",
                        accusative = "akademi",
                    ),
                    singularDefinite = declensions(
                        nominative = "akademia",
                        oblique = "akademisë",
                        accusative = "akademinë",
                    ),
                    pluralIndefinite = declensions(
                        nominative = "akademi",
                        oblique = "akademive",
                        accusative = "akademi",
                    ),
                    pluralDefinite = declensions(
                        nominative = "akademitë",
                        oblique = "akademive",
                        accusative = "akademitë",
                    ),
                ),
            ),
        )

        private fun declensions(
            nominative: String,
            oblique: String,
            accusative: String,
        ): AlbanianDeclensions = AlbanianDeclensions(
            nominative = NominativeDeclension(nominative),
            genitive = GenitiveDeclension(oblique),
            dative = DativeDeclension(oblique),
            accusative = AccusativeDeclension(accusative),
            ablative = AblativeDeclension(oblique),
        )
    }
}
