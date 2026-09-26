package com.cheatshqip

import android.app.Application
import com.cheatshqip.appupdate.adapter.output.CurrentActivityProvider
import com.cheatshqip.appupdate.di.appUpdateModule
import com.cheatshqip.di.applicationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CheatShqipApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val koinApplication = startKoin {
            androidContext(this@CheatShqipApplication)
            modules(applicationModule, appUpdateModule)
        }
        registerActivityLifecycleCallbacks(koinApplication.koin.get<CurrentActivityProvider>())
    }
}
