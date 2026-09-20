package com.cheatshqip.application

import com.cheatshqip.application.port.input.AlbanianWordDetailResult
import com.cheatshqip.application.port.input.GetAlbanianWordDetailUseCase
import com.cheatshqip.application.port.output.AlbanianNounEntryResult
import com.cheatshqip.application.port.output.GetAlbanianWordDetailPort
import com.cheatshqip.domain.AlbanianWordDetail
import com.cheatshqip.domain.NounParadigm
import com.cheatshqip.domain.Word

class AlbanianWordService(
    private val getAlbanianWordDetailPort: GetAlbanianWordDetailPort,
) : GetAlbanianWordDetailUseCase {
    override suspend fun getAlbanianWordDetail(albanianWord: Word): AlbanianWordDetailResult {
        return when (val result = getAlbanianWordDetailPort.getAlbanianWord(albanianWord)) {
            is AlbanianNounEntryResult.Found -> AlbanianWordDetailResult.Found(
                wordDetail = AlbanianWordDetail(
                    word = result.entry.word,
                    kind = result.entry.kind,
                    gender = result.entry.gender,
                    grammaticalDisplay = result.entry.grammaticalDisplay,
                    declensions = NounParadigm.from(result.entry.baseForms),
                ),
            )
            is AlbanianNounEntryResult.NotFound -> AlbanianWordDetailResult.NotFound
        }
    }
}
