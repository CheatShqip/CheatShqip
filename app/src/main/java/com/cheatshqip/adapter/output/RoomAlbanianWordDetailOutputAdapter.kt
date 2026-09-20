package com.cheatshqip.adapter.output

import com.cheatshqip.application.port.output.AlbanianNounEntryResult
import com.cheatshqip.application.port.output.GetAlbanianWordDetailPort
import com.cheatshqip.domain.AlbanianNounEntry
import com.cheatshqip.domain.GrammaticalDisplay
import com.cheatshqip.domain.NounBaseForms
import com.cheatshqip.domain.PluralGrammaticalDisplay
import com.cheatshqip.domain.SingularGrammaticalDisplay
import com.cheatshqip.domain.Word
import com.cheatshqip.domain.WordGender
import com.cheatshqip.domain.WordKind

class RoomAlbanianWordDetailOutputAdapter(
    private val dao: DictionaryDao,
) : GetAlbanianWordDetailPort {
    override suspend fun getAlbanianWord(word: Word): AlbanianNounEntryResult {
        val ascii = word.normalize().value.lowercase()
        val entity = dao.findByAscii(ascii) ?: return AlbanianNounEntryResult.NotFound
        return AlbanianNounEntryResult.Found(entity.toAlbanianNounEntry())
    }
}

private fun EntryEntity.toAlbanianNounEntry(): AlbanianNounEntry {
    val singularDisplay = compactForm?.substringBefore(' ') ?: albanianHeadword
    val pluralDisplay = nounPlIndef ?: ""
    val sgIndef = nounSgIndef ?: albanianHeadword
    val sgDef = nounSgDef ?: albanianHeadword
    val plIndef = nounPlIndef ?: albanianHeadword
    val plDef = nounPlDef ?: albanianHeadword
    return AlbanianNounEntry(
        word = Word(albanianHeadword),
        kind = WordKind.Name,
        gender = WordGender.Feminine,
        grammaticalDisplay = GrammaticalDisplay(
            singular = SingularGrammaticalDisplay(singularDisplay),
            plural = PluralGrammaticalDisplay(pluralDisplay),
        ),
        baseForms = NounBaseForms(
            singularIndefinite = sgIndef,
            singularDefinite = sgDef,
            pluralIndefinite = plIndef,
            pluralDefinite = plDef,
        ),
    )
}
