package com.cheatshqip.integration

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.cheatshqip.adapter.output.DictionaryDatabase
import com.cheatshqip.adapter.output.createDictionaryDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_DATABASE_NAME = "dictionary_test.db"

@RunWith(AndroidJUnit4::class)
class DictionaryDatabaseAssetTest {
    private lateinit var db: DictionaryDatabase

    @Before
    fun openDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.deleteDatabase(TEST_DATABASE_NAME)
        db = createDictionaryDatabase(context, TEST_DATABASE_NAME)
    }

    @After
    fun closeDatabase() {
        db.close()
    }

    @Test
    fun databaseAsset_entryCount_isSubstantial() = runBlocking {
        val count = db.dictionaryDao().countEntries()
        assertTrue("Expected > 10 000 entries, found $count", count > 10_000)
    }

    @Test
    fun databaseAsset_findByAscii_returnsKnownEntry() = runBlocking {
        val entry = db.dictionaryDao().findByAscii("pune")
        assertNotNull("Entry for normalized form 'pune' must exist in the bundled database", entry)
        assertEquals("punë", entry!!.albanianHeadword)
    }

    @Test
    fun databaseAsset_ftsPrefixSearch_returnsResultsForKnownPrefix() = runBlocking {
        val query = SimpleSQLiteQuery(
            "SELECT entry.* FROM entry, entry_fts " +
                "WHERE entry.rowid = entry_fts.rowid AND entry_fts.albanian_ascii MATCH ? LIMIT ?",
            arrayOf<Any>("pune*", 10),
        )
        val results = db.dictionaryDao().searchFts(query)
        assertTrue("FTS prefix 'pune*' should return at least one result", results.isNotEmpty())
        assertTrue(
            "All FTS results must have an ascii form starting with 'pune'",
            results.all { it.albanianAscii.startsWith("pune") },
        )
    }

    @Test
    fun databaseAsset_englishFtsSearch_returnsResultsForEnglishWord() = runBlocking {
        val query = SimpleSQLiteQuery(
            "SELECT entry.* FROM entry " +
                "JOIN entry_english ee ON ee.entry_rowid = entry.rowid " +
                "JOIN entry_english_fts fts ON fts.rowid = ee.id " +
                "WHERE entry_english_fts MATCH ? " +
                "GROUP BY entry.rowid " +
                "ORDER BY " +
                "  MIN(CASE WHEN ee.english = ? THEN 0 ELSE 1 END) ASC, " +
                "  (MIN(fts.rank) * 0.7) - (entry.frequency * 3.0) + (MIN(ee.position) * 0.1) ASC " +
                "LIMIT ?",
            arrayOf<Any>("\"work\"", "work", 10),
        )
        val results = db.dictionaryDao().searchByEnglishFts(query)
        assertTrue("FTS english search should return results for 'work'", results.isNotEmpty())
        assertEquals(
            "'punë' should be the first result for 'work'",
            "punë",
            results.first().albanianHeadword,
        )
    }

    @Test
    fun databaseAsset_metaTable_recordsSchemaVersion() = runBlocking {
        val query = SimpleSQLiteQuery(
            "SELECT value FROM meta WHERE key = ?",
            arrayOf<Any>("schema_version"),
        )
        val schemaVersion = db.dictionaryDao().getMetaValue(query)
        assertNotNull("meta table must contain a schema_version row", schemaVersion)
        assertEquals("1", schemaVersion)
    }
}
