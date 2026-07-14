package com.cheatshqip.adapter.output

import androidx.sqlite.db.SimpleSQLiteQuery
import com.cheatshqip.application.port.output.GetWordSuggestionsPort
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word

private const val SUGGESTIONS_LIMIT = 20

class SqliteWordSuggestionsOutputAdapter(
    private val dao: DictionaryDao,
) : GetWordSuggestionsPort {
    override suspend fun getWordSuggestionsOf(word: Word): List<Translation> {
        val term = "${word.normalize().value.lowercase()}*"
        val query = SimpleSQLiteQuery(
            "SELECT entry.* FROM entry, entry_fts " +
                "WHERE entry.rowid = entry_fts.rowid AND entry_fts.albanian_ascii MATCH ? " +
                "LIMIT ?",
            arrayOf<Any>(term, SUGGESTIONS_LIMIT),
        )
        return dao.searchFts(query).map { Translation(it.albanianHeadword) }
    }
}
