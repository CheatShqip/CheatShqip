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
            "SELECT * FROM entry " +
                "WHERE english = ? " +
                "OR english LIKE ? " +
                "OR english LIKE ? " +
                "OR english LIKE ? " +
                "LIMIT ?",
            arrayOf<Any>(
                word,
                "$word|",
                "|$word",
                "|$word|",
                MAX_RESULTS
            )
        )
        return dao.searchByEnglishToken(query)
            .map { Translation(it.albanianHeadword) }
    }
}
