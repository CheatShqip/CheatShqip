package com.cheatshqip

import android.app.Application
import com.cheatshqip.di.applicationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CheatShqipApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CheatShqipApplication)
            modules(applicationModule)
        }
    }
}