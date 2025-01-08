package com.cheatshqip.application

import com.cheatshqip.application.port.input.GetAlbanianWordDetailUseCase
import com.cheatshqip.domain.AlbanianWordDetail
import com.cheatshqip.domain.Word

class AlbanianWordService : GetAlbanianWordDetailUseCase {
    override suspend fun getAlbanianWordDetail(albanianWord: Word): AlbanianWordDetail {
        // todo : revoir le typage parce qu'un albanianword ne doit pas pouvoir être un englishword
        return AlbanianWordDetail.create()
    }
}
