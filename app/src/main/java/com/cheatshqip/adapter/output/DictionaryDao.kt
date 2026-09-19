package com.cheatshqip.adapter.output

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface DictionaryDao {
    @Query("SELECT * FROM entry WHERE albanian_ascii = :ascii LIMIT 1")
    suspend fun findByAscii(ascii: String): EntryEntity?

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
     * Full-text search over the english column using FTS5 BM25 ranking combined with
     * corpus frequency (ADR 0007). FTS5 tokenize='unicode61' treats '|' and ';' as
     * token separators, so searching "car" matches "car|automobile" but not "placard".
     *
     * Ranking formula: (rank * 0.7) - (frequency * 3.0) — 70% BM25 signal, 30% corpus
     * frequency signal. Higher frequency boosts an entry's ranking.
     *
     * Build the query with [androidx.sqlite.db.SimpleSQLiteQuery]:
     * ```
     * SimpleSQLiteQuery(
     *     "SELECT DISTINCT entry.* FROM entry, entry_fts " +
     *     "WHERE entry.rowid = entry_fts.rowid AND entry_fts.english MATCH ? " +
     *     "ORDER BY (rank * 0.7) - (frequency * 3.0) LIMIT ?",
     *     arrayOf(term, limit)
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
