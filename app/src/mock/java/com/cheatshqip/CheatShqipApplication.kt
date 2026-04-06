package com.cheatshqip

import android.app.Application
import com.cheatshqip.adapter.output.ApiBaseURL
import com.cheatshqip.application.port.output.GetAlbanianTranslationOfEnglishWordPort
import com.cheatshqip.di.applicationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class CheatShqipApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CheatShqipApplication)
            modules(applicationModule, mockModule)
        }
    }
}

private val mockModule = module {
    single<ApiBaseURL> {
        ApiBaseURL("http://localhost:9090/")
    }
    single<GetAlbanianTranslationOfEnglishWordPort> {
        FakeAlbanianTranslationOutputAdapter()
    }
}
