package com.cheatshqip.application.port.input

import com.cheatshqip.domain.AlbanianWordDetail
import com.cheatshqip.domain.Word

fun interface GetAlbanianWordDetailUseCase {
    suspend fun getAlbanianWordDetail(albanianWord: Word): AlbanianWordDetail
}
