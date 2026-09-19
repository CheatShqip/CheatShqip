package com.cheatshqip.application

import com.cheatshqip.application.port.input.GetAlbanianWordDetailUseCase
import com.cheatshqip.application.port.output.GetAlbanianWordDetailPort
import com.cheatshqip.domain.AlbanianWordDetail
import com.cheatshqip.domain.NounParadigm
import com.cheatshqip.domain.Word

class AlbanianWordService(
    private val getAlbanianWordDetailPort: GetAlbanianWordDetailPort,
) : GetAlbanianWordDetailUseCase {
    override suspend fun getAlbanianWordDetail(albanianWord: Word): AlbanianWordDetail {
        val entry = getAlbanianWordDetailPort.getAlbanianWord(albanianWord)
        return AlbanianWordDetail(
            word = entry.word,
            kind = entry.kind,
            gender = entry.gender,
            grammaticalDisplay = entry.grammaticalDisplay,
            declensions = NounParadigm.from(entry.baseForms),
        )
    }
}
