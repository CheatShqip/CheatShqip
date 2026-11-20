package com.cheatshqip.adapter.output

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface DictionaryDao {
    @Query(
        "SELECT * FROM entry WHERE albanian_ascii = :ascii " +
            "ORDER BY CASE WHEN pos = 'noun' THEN 0 ELSE 1 END, frequency DESC LIMIT 1"
    )
    suspend fun findByAscii(ascii: String): EntryEntity?

    @Query(
        "SELECT * FROM entry WHERE albanian_ascii = :ascii " +
            "ORDER BY " +
            "  CASE WHEN :headword != :ascii AND albanian_headword = :headword THEN 0 ELSE 1 END, " +
            "  CASE WHEN pos = 'noun' THEN 0 ELSE 1 END, " +
            "  frequency DESC " +
            "LIMIT 1"
    )
    suspend fun findByHeadwordOrAscii(ascii: String, headword: String): EntryEntity?

    @Query("SELECT COUNT(*) FROM entry")
    suspend fun countEntries(): Int

    /**
     * The `meta` table is not a Room [androidx.room.Entity], so it must be queried with
     * [RawQuery]: `SimpleSQLiteQuery("SELECT value FROM meta WHERE key = ?", arrayOf(key))`.
     */
    @RawQuery
    suspend fun getMetaValue(query: SupportSQLiteQuery): String?

    /**
     * Full-text search across the pre-built entry_fts FTS5 table, scoped to the
     * albanian_ascii column so prefixes only match Albanian headwords, not english/gloss text.
     *
     * Build the query with [androidx.sqlite.db.SimpleSQLiteQuery]:
     * ```
     * SimpleSQLiteQuery(
     *     "SELECT entry.* FROM entry, entry_fts " +
     *     "WHERE entry.rowid = entry_fts.rowid AND entry_fts.albanian_ascii MATCH ? LIMIT ?",
     *     arrayOf(term, limit)
     * )
     * ```
     *
     * Use [RawQuery] because Room's compile-time parser does not understand FTS5
     * virtual-table syntax in [Query].
     */
    @RawQuery
    suspend fun searchFts(query: SupportSQLiteQuery): List<EntryEntity>

    /**
     * Full-text search over the normalized entry_english / entry_english_fts tables.
     *
     * Each English translation is stored as a separate row with its own `position`
     * (sense index), so no pipe-delimited strings are parsed in SQL. Exact-match
     * evaluation uses `ee.english = :word`, which is true for ANY sense of the entry
     * (e.g. "car" matches makinë = machine|vehicle|car), while compounds like
     * "water jug" are ranked below.
     *
     * Ranking (per entry):
     *   MIN(CASE WHEN ee.english = :word THEN 0 ELSE 1 END),
     *   (MIN(fts.rank) * 0.7) - (entry.frequency * 3.0) + (MIN(ee.position) * 0.1)
     *
     * Build the query with [androidx.sqlite.db.SimpleSQLiteQuery] (the FTS match
     * value is wrapped in double quotes to behave as an exact phrase):
     * ```
     * SimpleSQLiteQuery(
     *     "SELECT entry.* FROM entry " +
     *     "JOIN entry_english ee ON ee.entry_rowid = entry.rowid " +
     *     "JOIN entry_english_fts fts ON fts.rowid = ee.id " +
     *     "WHERE entry_english_fts MATCH ? " +
     *     "GROUP BY entry.rowid " +
     *     "ORDER BY " +
     *     "  MIN(CASE WHEN ee.english = ? THEN 0 ELSE 1 END) ASC, " +
     *     "  (MIN(fts.rank) * 0.7) - (entry.frequency * 3.0) + (MIN(ee.position) * 0.1) ASC " +
     *     "LIMIT ?",
     *     arrayOf("\"$word\"", word, limit)
     * )
     * ```
     *
     * Use [RawQuery] because Room's compile-time parser does not understand FTS5
     * virtual-table syntax in [Query].
     */
    @RawQuery
    suspend fun searchByEnglishFts(query: SupportSQLiteQuery): List<EntryEntity>

    /** @deprecated Use [searchByEnglishFts] instead */
    @RawQuery
    suspend fun searchByEnglishToken(query: SupportSQLiteQuery): List<EntryEntity>
}
