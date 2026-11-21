package com.cheatshqip.adapter.output

import androidx.sqlite.db.SimpleSQLiteQuery
import com.cheatshqip.application.port.output.GetEnglishToAlbanianTranslationsPort
import com.cheatshqip.domain.EnglishSearchKeys
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word

private const val MAX_RESULTS = 20
private const val RANK_WEIGHT = 0.7
private const val FREQUENCY_WEIGHT = 3.0
private const val POSITION_WEIGHT = 0.1

class SqliteEnglishToAlbanianOutputAdapter(
    private val dao: DictionaryDao,
) : GetEnglishToAlbanianTranslationsPort {
    override suspend fun getTranslationsForEnglishWord(englishWord: Word): List<Translation> {
        val keys = EnglishSearchKeys.from(englishWord.value)
        if (keys.compact.isEmpty()) return emptyList()

        val entries = dao.findByEnglishKeys(exactQuery(keys))
            .ifEmpty { searchFallback(keys) }
        return entries.map { entry -> Translation(entry.albanianHeadword) }.distinct()
    }

    private fun exactQuery(keys: EnglishSearchKeys) = SimpleSQLiteQuery(
        "SELECT entry.* FROM entry " +
            "JOIN english_search es ON es.entry_rowid = entry.rowid " +
            "WHERE es.term IN (?, ?) " +
            "GROUP BY entry.rowid " +
            "ORDER BY " +
            "  MIN(CASE WHEN es.term = ? THEN 0 ELSE 1 END) ASC, " +
            "  entry.frequency DESC, " +
            "  MIN(es.position) ASC, " +
            "  CASE WHEN entry.pos = 'noun' THEN 0 ELSE 1 END " +
            "LIMIT ?",
        arrayOf<Any>(keys.canonical, keys.compact, keys.canonical, MAX_RESULTS),
    )

    private suspend fun searchFallback(keys: EnglishSearchKeys): List<EntryEntity> {
        val phrase = dao.searchEnglishSearchFts(ftsQuery("\"${keys.canonical}\"", keys))
        if (phrase.isNotEmpty()) return phrase

        val prefix = dao.searchEnglishSearchFts(ftsQuery("${keys.compact}*", keys))
        return prefix
    }

    private fun ftsQuery(match: String, keys: EnglishSearchKeys) = SimpleSQLiteQuery(
        "SELECT entry.* FROM entry " +
            "JOIN english_search es ON es.entry_rowid = entry.rowid " +
            "JOIN english_search_fts fts ON fts.rowid = es.id " +
            "WHERE english_search_fts MATCH ? " +
            "GROUP BY entry.rowid " +
            "ORDER BY " +
            "  MIN(CASE WHEN es.term = ? THEN 0 WHEN es.term = ? THEN 1 ELSE 2 END) ASC, " +
            "  (MIN(fts.rank) * $RANK_WEIGHT) - (entry.frequency * $FREQUENCY_WEIGHT) + " +
            "  (MIN(es.position) * $POSITION_WEIGHT) ASC " +
            "LIMIT ?",
        arrayOf<Any>(match, keys.canonical, keys.compact, MAX_RESULTS),
    )
}
