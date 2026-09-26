package com.cheatshqip.appupdate.adapter.output

import com.cheatshqip.appupdate.application.port.output.InstallUpdateDeliveryPort
import com.cheatshqip.appupdate.application.port.output.InstallUpdateDeliveryResult
import com.google.android.gms.common.api.ApiException
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.ktx.requestCompleteUpdate
import kotlinx.coroutines.CancellationException

class PlayInstallUpdateDeliveryOutputAdapter(
    private val appUpdateManager: AppUpdateManager,
) : InstallUpdateDeliveryPort {
    override suspend fun installUpdateDelivery(): InstallUpdateDeliveryResult = try {
        appUpdateManager.requestCompleteUpdate()
        InstallUpdateDeliveryResult.Started
    } catch (e: CancellationException) {
        throw e
    } catch (ignored: ApiException) {
        InstallUpdateDeliveryResult.Failed
    }
}
