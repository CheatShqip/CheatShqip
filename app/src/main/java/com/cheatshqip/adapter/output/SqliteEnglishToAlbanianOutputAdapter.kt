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
        val word = englishWord.normalize().value
        val query = SimpleSQLiteQuery(
            "SELECT DISTINCT entry.* FROM entry, entry_fts " +
                "WHERE entry.rowid = entry_fts.rowid " +
                "AND entry_fts.english MATCH ? " +
                "ORDER BY " +
                "  CASE " +
                "    WHEN (" +
                "      CASE " +
                "        WHEN INSTR(entry.english, '|') > 0 " +
                "          THEN TRIM(SUBSTR(entry.english, 1, " +
                "            INSTR(entry.english, '|') - 1)) " +
                "        ELSE entry.english " +
                "      END" +
                "    ) = ? THEN 0 " +
                "    ELSE 1 " +
                "  END, " +
                "  (rank * 0.7) - (frequency * 3.0) " +
                "LIMIT ?",
            arrayOf<Any>(word, word, MAX_RESULTS)
        )
        return dao.searchByEnglishFts(query)
            .map { Translation(it.albanianHeadword) }
    }
}
