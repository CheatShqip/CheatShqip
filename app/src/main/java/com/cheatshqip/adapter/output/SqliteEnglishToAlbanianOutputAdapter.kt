package com.cheatshqip.adapter.output

import com.cheatshqip.application.port.output.GetEnglishToAlbanianTranslationsPort
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word

private const val MAX_RESULTS = 20

class SqliteEnglishToAlbanianOutputAdapter(
    private val dao: DictionaryDao,
) : GetEnglishToAlbanianTranslationsPort {
    override suspend fun getTranslationsForEnglishWord(englishWord: Word): List<Translation> {
        val word = englishWord.normalize().value.lowercase().trim()
        if (word.isEmpty()) return emptyList()
        return dao.findByEnglishExact(word, MAX_RESULTS)
            .map { entry -> Translation(entry.albanianHeadword) }
            .distinct()
    }
}
