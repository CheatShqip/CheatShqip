package com.cheatshqip.appupdate.di

import com.cheatshqip.appupdate.adapter.output.CurrentActivityProvider
import com.cheatshqip.appupdate.adapter.output.PlayInstallUpdateDeliveryOutputAdapter
import com.cheatshqip.appupdate.adapter.output.PlayStartUpdateDeliveryOutputAdapter
import com.cheatshqip.appupdate.adapter.output.PlayUpdateFlagsOutputAdapter
import com.cheatshqip.appupdate.adapter.output.PlayUpdateInfoMapper
import com.cheatshqip.appupdate.adapter.output.PlayUpdateKindThresholds
import com.cheatshqip.appupdate.application.CheckForUpdateService
import com.cheatshqip.appupdate.application.InstallUpdateService
import com.cheatshqip.appupdate.application.StartUpdateService
import com.cheatshqip.appupdate.application.port.input.CheckForUpdateUseCase
import com.cheatshqip.appupdate.application.port.input.InstallUpdateUseCase
import com.cheatshqip.appupdate.application.port.input.StartUpdateUseCase
import com.cheatshqip.appupdate.application.port.output.GetUpdateFlagsPort
import com.cheatshqip.appupdate.application.port.output.InstallUpdateDeliveryPort
import com.cheatshqip.appupdate.application.port.output.StartUpdateDeliveryPort
import com.cheatshqip.appupdate.presentation.AppUpdateGateViewModel
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appUpdateModule = module {
    single { AppUpdateManagerFactory.create(androidContext()) }

    single { CurrentActivityProvider() }

    single { PlayUpdateKindThresholds() }

    single { PlayUpdateInfoMapper() }

    single<GetUpdateFlagsPort> {
        PlayUpdateFlagsOutputAdapter(
            appUpdateManager = get(),
            mapper = get(),
            thresholds = get(),
        )
    }

    single<StartUpdateDeliveryPort> {
        PlayStartUpdateDeliveryOutputAdapter(
            appUpdateManager = get(),
            currentActivityProvider = get(),
            mapper = get(),
        )
    }

    single<InstallUpdateDeliveryPort> {
        PlayInstallUpdateDeliveryOutputAdapter(appUpdateManager = get())
    }

    single<CheckForUpdateUseCase> {
        CheckForUpdateService(getUpdateFlagsPort = get())
    }

    single<StartUpdateUseCase> {
        StartUpdateService(startUpdateDeliveryPort = get())
    }

    single<InstallUpdateUseCase> {
        InstallUpdateService(installUpdateDeliveryPort = get())
    }

    viewModel {
        AppUpdateGateViewModel(
            coroutineDispatcher = get(),
            checkForUpdateUseCase = get(),
            startUpdateUseCase = get(),
            installUpdateUseCase = get(),
        )
    }
}
