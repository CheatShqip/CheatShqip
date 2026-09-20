package com.cheatshqip.integration

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.cheatshqip.adapter.output.DictionaryDatabase
import com.cheatshqip.adapter.output.SqliteEnglishToAlbanianOutputAdapter
import com.cheatshqip.adapter.output.createDictionaryDatabase
import com.cheatshqip.domain.Word
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_DATABASE_NAME = "dictionary_test.db"

@RunWith(AndroidJUnit4::class)
class SqliteEnglishToAlbanianOutputAdapterIntegrationTest {
    private lateinit var db: DictionaryDatabase
    private lateinit var adapter: SqliteEnglishToAlbanianOutputAdapter

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.deleteDatabase(TEST_DATABASE_NAME)
        db = createDictionaryDatabase(context, TEST_DATABASE_NAME)
        adapter = SqliteEnglishToAlbanianOutputAdapter(db.dictionaryDao())
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun given_work_from_pipe_and_semicolon_separated_meanings_should_return_pune() = runBlocking {
        val result = adapter.getTranslationsForEnglishWord(Word("work"))

        assertTrue("Should find at least one translation for work", result.isNotEmpty())
        assertEquals(
            "'punë' should be in top 3 translations for 'work'",
            "punë",
            result.take(3).first { it.value == "punë" }.value,
        )
    }

    @Test
    fun given_labour_from_pipe_and_semicolon_separated_meanings_should_return_pune() = runBlocking {
        val result = adapter.getTranslationsForEnglishWord(Word("labour"))

        assertTrue("Should find at least one translation for labour", result.isNotEmpty())
        assertTrue("Should contain pune translation", result.any { it.value == "punë" })
    }

    @Test
    fun given_job_from_pipe_and_semicolon_separated_meanings_should_return_pune() = runBlocking {
        val result = adapter.getTranslationsForEnglishWord(Word("job"))

        assertTrue("Should find at least one translation for job", result.isNotEmpty())
        assertTrue("Should contain pune translation", result.any { it.value == "punë" })
    }

    @Test
    fun given_normalized_WORK_should_return_pune() = runBlocking {
        val result = adapter.getTranslationsForEnglishWord(Word("WORK"))

        assertTrue("Should find at least one translation for WORK", result.isNotEmpty())
        assertTrue("Should contain pune translation", result.any { it.value == "punë" })
    }

    @Test
    fun given_either_from_pipe_separated_meanings_should_return_translation() = runBlocking {
        val result = adapter.getTranslationsForEnglishWord(Word("either"))

        assertTrue("Should find at least one translation for either", result.isNotEmpty())
    }

    @Test
    fun given_or_from_pipe_separated_meanings_should_return_translation() = runBlocking {
        val result = adapter.getTranslationsForEnglishWord(Word("or"))

        assertTrue("Should find at least one translation for or", result.isNotEmpty())
    }

    @Test
    fun given_water_should_rank_pure_translation_uje_above_compound_water_jug() =
        runBlocking {
            val result = adapter.getTranslationsForEnglishWord(Word("water"))

            assertTrue("Should find translations for water", result.isNotEmpty())
            val ujeTranslation = result.first { it.value == "ujë" }
            val compoundTranslation = result.firstOrNull { it.value == "kanë" }

            val ujeIndex = result.indexOf(ujeTranslation)
            val kanIndex = compoundTranslation?.let { result.indexOf(it) } ?: Int.MAX_VALUE

            assertTrue(
                "'ujë' (pure translation) should rank above 'kanë' (compound: water jug), " +
                    "ujë index=$ujeIndex, kanë index=$kanIndex",
                ujeIndex < kanIndex,
            )
        }

    @Test
    fun given_orange_should_rank_pure_portokall_above_compound_paleorange() =
        runBlocking {
            val result = adapter.getTranslationsForEnglishWord(Word("orange"))

            assertTrue("Should find translations for orange", result.isNotEmpty())
            val portokallTranslation = result.first { it.value == "portokall" }
            val compoundTranslation = result.firstOrNull { it.value == "mollët" }

            val portokallIndex = result.indexOf(portokallTranslation)
            val mollletIndex =
                compoundTranslation?.let { result.indexOf(it) } ?: Int.MAX_VALUE

            assertTrue(
                "'portokall' (pure translation) should rank above 'mollët' (compound: pale orange), " +
                    "portokallIndex=$portokallIndex, mollletIndex=$mollletIndex",
                portokallIndex < mollletIndex,
            )
        }

    @Test
    fun given_oil_should_rank_pure_vaj_above_compound_oil_lamp() =
        runBlocking {
            val result = adapter.getTranslationsForEnglishWord(Word("oil"))

            assertTrue("Should find translations for oil", result.isNotEmpty())
            val vajTranslation = result.first { it.value == "vaj" }
            val compoundTranslation = result.firstOrNull { it.value == "drite" }

            val vajIndex = result.indexOf(vajTranslation)
            val driteIndex = compoundTranslation?.let { result.indexOf(it) } ?: Int.MAX_VALUE

            assertTrue(
                "'vaj' (pure translation) should rank above 'drite' (compound: oil lamp), " +
                    "vajIndex=$vajIndex, driteIndex=$driteIndex",
                vajIndex < driteIndex,
            )
        }

    @Test
    fun given_first_token_with_pipe_should_use_first_meaning_only() =
        runBlocking {
            val result = adapter.getTranslationsForEnglishWord(Word("water"))

            assertTrue("Should find translations for water", result.isNotEmpty())
            val ujit = result.firstOrNull { it.value == "ujit" }

            // ujit has english="water|to irrigate" — first token is "water", a pure match
            // It should rank in tier 0 alongside ujë
            if (ujit != null) {
                val ujitIndex = result.indexOf(ujit)
                val ujeIndex = result.indexOf(result.first { it.value == "ujë" })
                // Both are tier 0 (first token == "water"), so order by frequency
                // uje (10.17) > ujit (9.63), so uje should come first
                assertTrue(
                    "ujit (first_token='water') should be a tier 0 match, " +
                        "ranking close to ujë",
                    kotlin.math.abs(ujeIndex - ujitIndex) <= 1,
                )
            }
        }
}
