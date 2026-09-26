package com.cheatshqip.appupdate.application

import com.cheatshqip.appupdate.application.port.input.CheckForUpdateResult
import com.cheatshqip.appupdate.application.port.input.CheckForUpdateUseCase
import com.cheatshqip.appupdate.application.port.output.GetUpdateFlagsPort
import com.cheatshqip.appupdate.application.port.output.GetUpdateFlagsResult

class CheckForUpdateService(
    private val getUpdateFlagsPort: GetUpdateFlagsPort,
) : CheckForUpdateUseCase {
    override suspend fun checkForUpdate(): CheckForUpdateResult =
        when (val result = getUpdateFlagsPort.getUpdateFlags()) {
            is GetUpdateFlagsResult.Available -> CheckForUpdateResult.UpdateAvailable(result.flags)
            GetUpdateFlagsResult.NotAvailable -> CheckForUpdateResult.NoUpdate
            GetUpdateFlagsResult.Failed -> CheckForUpdateResult.CheckFailed
        }
}
