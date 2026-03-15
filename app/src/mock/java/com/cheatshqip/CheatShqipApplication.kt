package com.cheatshqip

import android.app.Application
import com.cheatshqip.adapter.output.ApiBaseURL
import com.cheatshqip.application.port.output.GetAlbanianTranslationOfEnglishWordPort
import com.cheatshqip.di.applicationModule
import com.cheatshqip.fixtures.KARTE_JSON_RESPONSE
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.net.InetAddress

private const val MOCK_SERVER_PORT = 8099

class CheatShqipApplication : Application() {

    private val mockWebServer = MockWebServer()

    override fun onCreate() {
        super.onCreate()

        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse =
                when {
                    request.path?.contains("/define/karte") == true ->
                        MockResponse().setResponseCode(200).setBody(KARTE_JSON_RESPONSE)
                    else -> MockResponse().setResponseCode(404)
                }
        }
        mockWebServer.start(InetAddress.getByAddress(byteArrayOf(127, 0, 0, 1)), MOCK_SERVER_PORT)

        val mockModule = module {
            single<ApiBaseURL> {
                ApiBaseURL("http://127.0.0.1:$MOCK_SERVER_PORT/")
            }
            single<GetAlbanianTranslationOfEnglishWordPort> {
                FakeAlbanianTranslationOutputAdapter()
            }
        }

        startKoin {
            allowOverride(true)
            androidContext(this@CheatShqipApplication)
            modules(applicationModule, mockModule)
        }
    }
}
