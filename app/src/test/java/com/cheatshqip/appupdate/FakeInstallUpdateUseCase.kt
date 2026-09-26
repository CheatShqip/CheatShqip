package com.cheatshqip.appupdate

import com.cheatshqip.appupdate.application.port.input.InstallUpdateResult
import com.cheatshqip.appupdate.application.port.input.InstallUpdateUseCase

class FakeInstallUpdateUseCase : InstallUpdateUseCase {
    var result: InstallUpdateResult = InstallUpdateResult.Failed
    var installCount: Int = 0

    override suspend fun installUpdate(): InstallUpdateResult {
        installCount++
        return result
    }
}
