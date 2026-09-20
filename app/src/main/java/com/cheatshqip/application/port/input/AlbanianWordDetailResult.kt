package com.cheatshqip.application.port.input

import com.cheatshqip.domain.AlbanianWordDetail

sealed interface AlbanianWordDetailResult {
    data class Found(val wordDetail: AlbanianWordDetail) : AlbanianWordDetailResult
    data object NotFound : AlbanianWordDetailResult
}
