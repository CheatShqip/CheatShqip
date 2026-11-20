package com.cheatshqip.adapter.output

import androidx.sqlite.db.SimpleSQLiteQuery
import com.cheatshqip.application.port.output.GetEnglishToAlbanianTranslationsPort
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word

private const val MAX_RESULTS = 20

class SqliteEnglishToAlbanianOutputAdapter(
    private val dao: DictionaryDao,
) : GetEnglishToAlbanianTranslationsPort {
    override suspend fun getTranslationsForEnglishWord(englishWord: Word): List<Translation> {
        val word = englishWord.normalize().value.lowercase().trim()
        val ftsQuery = toFtsPhraseQuery(word)
        if (ftsQuery.isEmpty()) return emptyList()
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
            arrayOf<Any>(ftsQuery, word, MAX_RESULTS),
        )
        return dao.searchByEnglishFts(query)
            .map { entry -> Translation(entry.albanianHeadword) }
            .distinct()
    }

    private fun toFtsPhraseQuery(word: String): String {
        val sanitized = word.replace("\"", "")
        if (sanitized.isBlank()) return ""
        return "\"$sanitized\""
    }
}
