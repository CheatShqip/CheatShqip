package com.cheatshqip.appupdate

import com.cheatshqip.appupdate.application.port.input.CheckForUpdateResult
import com.cheatshqip.appupdate.application.port.input.CheckForUpdateUseCase

class FakeCheckForUpdateUseCase : CheckForUpdateUseCase {
    var result: CheckForUpdateResult = CheckForUpdateResult.CheckFailed
    var checkCount: Int = 0

    override suspend fun checkForUpdate(): CheckForUpdateResult {
        checkCount++
        return result
    }
}
