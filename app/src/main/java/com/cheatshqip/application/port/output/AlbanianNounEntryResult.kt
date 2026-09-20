package com.cheatshqip.application.port.output

import com.cheatshqip.domain.AlbanianNounEntry

sealed interface AlbanianNounEntryResult {
    data class Found(val entry: AlbanianNounEntry) : AlbanianNounEntryResult
    data object NotFound : AlbanianNounEntryResult
}
