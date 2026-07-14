package com.cheatshqip.integration

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.cheatshqip.adapter.output.DictionaryDatabase
import com.cheatshqip.adapter.output.SqliteEnglishToAlbanianOutputAdapter
import com.cheatshqip.adapter.output.createDictionaryDatabase
import com.cheatshqip.domain.Translation
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
        assertTrue("Should contain pune translation", result.any { it.value == "punë" })
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
}
