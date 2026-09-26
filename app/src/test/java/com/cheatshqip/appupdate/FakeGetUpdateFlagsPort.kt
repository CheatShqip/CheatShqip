package com.cheatshqip.appupdate

import com.cheatshqip.appupdate.application.port.output.GetUpdateFlagsPort
import com.cheatshqip.appupdate.application.port.output.GetUpdateFlagsResult

class FakeGetUpdateFlagsPort : GetUpdateFlagsPort {
    var result: GetUpdateFlagsResult = GetUpdateFlagsResult.Failed

    override suspend fun getUpdateFlags(): GetUpdateFlagsResult = result
}
