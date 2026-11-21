package com.cheatshqip

import com.cheatshqip.domain.EnglishSearchKeys
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class EnglishSearchKeysTest {
    @Test
    fun `given hyphenated or spaced spellings, they should share the compact key`() {
        assertEquals(EnglishSearchKeys("letter box", "letterbox"), EnglishSearchKeys.from("letter-box"))
        assertEquals(EnglishSearchKeys("letter box", "letterbox"), EnglishSearchKeys.from("letter box"))
    }

    @Test
    fun `given concatenated spelling, it should share the compact key`() {
        assertEquals(EnglishSearchKeys("coffeecup", "coffeecup"), EnglishSearchKeys.from("coffeecup"))
        assertEquals(EnglishSearchKeys("coffee cup", "coffeecup"), EnglishSearchKeys.from("coffee cup"))
    }

    @Test
    fun `given OCR hyphen space, it should be collapsed`() {
        assertEquals(
            EnglishSearchKeys("pi llow case", "pillowcase"),
            EnglishSearchKeys.from("pi- llow case"),
        )
        assertEquals(
            EnglishSearchKeys("pillow case", "pillowcase"),
            EnglishSearchKeys.from("pillow case"),
        )
    }

    @Test
    fun `given OCR split word, it should share the compact key`() {
        assertEquals(
            EnglishSearchKeys("wed ding present", "weddingpresent"),
            EnglishSearchKeys.from("wed ding present"),
        )
        assertEquals(
            EnglishSearchKeys("wedding present", "weddingpresent"),
            EnglishSearchKeys.from("wedding present"),
        )
    }

    @Test
    fun `given accents, they should be folded`() {
        assertEquals(EnglishSearchKeys("cafe", "cafe"), EnglishSearchKeys.from("Café"))
    }

    @Test
    fun `given unicode dashes and apostrophes, they should be normalized`() {
        assertEquals(
            EnglishSearchKeys("rock and roll", "rockandroll"),
            EnglishSearchKeys.from("rock–and–roll"),
        )
        assertEquals(EnglishSearchKeys("don't", "dont"), EnglishSearchKeys.from("don’t"))
    }

    @Test
    fun `given extra whitespace, it should be collapsed and trimmed`() {
        assertEquals(EnglishSearchKeys("new york", "newyork"), EnglishSearchKeys.from("  New   York  "))
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "   ", "---"])
    fun `given term without letters, keys should be empty`(term: String) {
        assertEquals(EnglishSearchKeys("", ""), EnglishSearchKeys.from(term))
    }
}
