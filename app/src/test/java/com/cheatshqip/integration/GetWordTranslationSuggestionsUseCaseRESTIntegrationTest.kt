package com.cheatshqip.integration

import com.cheatshqip.FakeAlbanianTranslationOutputAdapter
import com.cheatshqip.adapter.output.ApiBaseURL
import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.application.port.output.GetAlbanianTranslationOfEnglishWordPort
import com.cheatshqip.di.applicationModule
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word
import com.cheatshqip.integration.fixtures.PUNE_JSON_RESPONSE
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

class GetWordTranslationSuggestionsUseCaseRESTIntegrationTest : KoinTest {
    private val useCase: GetWordTranslationSuggestionsUseCase by inject()
    private val mockWebServer = MockWebServer()

    @BeforeEach
    fun setUp() {
        mockWebServer.start()
        val baseURL = mockWebServer.url("/").toString()
        val baseURLModule =
            module {
            single<ApiBaseURL> { ApiBaseURL(baseURL) }
            single<GetAlbanianTranslationOfEnglishWordPort> { FakeAlbanianTranslationOutputAdapter() }
        }
        startKoin {
            modules(applicationModule, baseURLModule)
        }
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `given some characters, should propose translations`() =
        runTest {
        val jsonResponse = PUNE_JSON_RESPONSE

        mockWebServer.dispatcher =
            object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.path) {
                    "/define/pune" ->
                        MockResponse()
                        .setResponseCode(200)
                        .setBody(jsonResponse)
                        .addHeader("Content-Type", "application/json")

                    else -> MockResponse().setResponseCode(404)
                }
            }
        }

        val result = useCase.getWorldTranslationSuggestions(Word("work"))

        assertEquals(
            listOf(
                Translation("punë"),
                Translation("pufe"),
                Translation("pure"),
                Translation("arne"),
                Translation("buçe")
            ),
            result
        )
    }
}
