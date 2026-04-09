package com.cheatshqip.application

import com.cheatshqip.application.port.input.GetAlbanianWordDetailUseCase
import com.cheatshqip.application.port.output.GetAlbanianWordDetailPort
import com.cheatshqip.domain.AlbanianWordDetail
import com.cheatshqip.domain.Word

class AlbanianWordService(
    private val getAlbanianWordDetailPort: GetAlbanianWordDetailPort,
) : GetAlbanianWordDetailUseCase {
    override suspend fun getAlbanianWordDetail(albanianWord: Word): AlbanianWordDetail =
        getAlbanianWordDetailPort.getAlbanianWordDetail(albanianWord)
}
