package com.cheatshqip.appupdate

import com.cheatshqip.appupdate.application.port.input.StartUpdateResult
import com.cheatshqip.appupdate.application.port.input.StartUpdateUseCase
import com.cheatshqip.appupdate.domain.UpdateKind

class FakeStartUpdateUseCase : StartUpdateUseCase {
    var result: StartUpdateResult = StartUpdateResult.Failed
    var lastKind: UpdateKind? = null

    override suspend fun startUpdate(kind: UpdateKind): StartUpdateResult {
        lastKind = kind
        return result
    }
}
