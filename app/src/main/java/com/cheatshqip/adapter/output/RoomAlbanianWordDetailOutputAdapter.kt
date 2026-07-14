package com.cheatshqip.adapter.output

import com.cheatshqip.application.port.output.GetAlbanianWordDetailPort
import com.cheatshqip.domain.AblativeDeclension
import com.cheatshqip.domain.AccusativeDeclension
import com.cheatshqip.domain.AlbanianDeclensions
import com.cheatshqip.domain.AlbanianWordDetail
import com.cheatshqip.domain.DativeDeclension
import com.cheatshqip.domain.GenitiveDeclension
import com.cheatshqip.domain.GrammaticalDisplay
import com.cheatshqip.domain.NominativeDeclension
import com.cheatshqip.domain.PluralGrammaticalDisplay
import com.cheatshqip.domain.SingularGrammaticalDisplay
import com.cheatshqip.domain.Word
import com.cheatshqip.domain.WordGender
import com.cheatshqip.domain.WordKind

class RoomAlbanianWordDetailOutputAdapter(
    private val dao: DictionaryDao,
) : GetAlbanianWordDetailPort {
    override suspend fun getAlbanianWordDetail(word: Word): AlbanianWordDetail {
        val ascii = word.normalize().value.lowercase()
        val entity = dao.findByAscii(ascii)
            ?: error("No detail found for word: ${word.value}")
        return entity.toAlbanianWordDetail()
    }
}

private fun EntryEntity.toAlbanianWordDetail(): AlbanianWordDetail {
    val singularDisplay = compactForm?.substringBefore(' ') ?: albanianHeadword
    val pluralDisplay = nounPlIndef ?: ""
    val sgDef = nounSgDef ?: albanianHeadword
    return AlbanianWordDetail(
        word = Word(albanianHeadword),
        kind = WordKind.Name,
        gender = WordGender.Feminine,
        grammaticalDisplay = GrammaticalDisplay(
            singular = SingularGrammaticalDisplay(singularDisplay),
            plural = PluralGrammaticalDisplay(pluralDisplay),
        ),
        singularDefiniteDeclensions = AlbanianDeclensions(
            nominative = NominativeDeclension(sgDef),
            genitive = GenitiveDeclension(""),
            dative = DativeDeclension(""),
            accusative = AccusativeDeclension(""),
            ablative = AblativeDeclension(""),
        ),
    )
}
