package com.cheatshqip.appupdate.adapter.output

import com.cheatshqip.appupdate.application.port.output.GetUpdateFlagsPort
import com.cheatshqip.appupdate.application.port.output.GetUpdateFlagsResult
import com.google.android.gms.common.api.ApiException
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.ktx.requestAppUpdateInfo
import kotlinx.coroutines.CancellationException

class PlayUpdateFlagsOutputAdapter(
    private val appUpdateManager: AppUpdateManager,
    private val mapper: PlayUpdateInfoMapper,
    private val thresholds: PlayUpdateKindThresholds,
) : GetUpdateFlagsPort {
    override suspend fun getUpdateFlags(): GetUpdateFlagsResult = try {
        val appUpdateInfo = appUpdateManager.requestAppUpdateInfo()
        mapper.mapFlags(
            updateAvailability = appUpdateInfo.updateAvailability(),
            updatePriority = appUpdateInfo.updatePriority(),
            availableVersionCode = appUpdateInfo.availableVersionCode(),
            installStatus = appUpdateInfo.installStatus(),
            thresholds = thresholds,
        )
    } catch (e: CancellationException) {
        throw e
    } catch (ignored: ApiException) {
        GetUpdateFlagsResult.Failed
    }
}
