package com.cheatshqip.di

import com.cheatshqip.BuildConfig
import com.cheatshqip.HomeScreenViewModel
import com.cheatshqip.adapter.output.ApiBaseURL
import com.cheatshqip.adapter.output.RESTWordSuggestionsOutputAdapter
import com.cheatshqip.adapter.output.ShqipRESTService
import com.cheatshqip.application.TranslationService
import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.application.port.output.GetAlbanianTranslationOfEnglishWordPort
import com.cheatshqip.application.port.output.GetWordSuggestionsPort
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val HTTP_APPLICATION_JSON = "application/json"

private val jsonBuilder = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

val applicationModule = module {
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .build()
    }

    single<ApiBaseURL> {
        ApiBaseURL(BuildConfig.API_BASE_URL)
    }

    single<Converter.Factory> {
        HTTP_APPLICATION_JSON
            .toMediaType()
            .let(jsonBuilder::asConverterFactory)
    }

    single<CoroutineDispatcher> {
        Dispatchers.IO
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(get<ApiBaseURL>().value)
            .client(get())
            .addConverterFactory(get())
            .build()
    }

    single<ShqipRESTService> {
        get<Retrofit>().create<ShqipRESTService>(ShqipRESTService::class.java)
    }

    single<GetWordTranslationSuggestionsUseCase> {
        TranslationService(
            getAlbanianTranslationOfEnglishWordPort = get(),
            getWordSuggestionsPort = get()
        )
    }

    single<GetWordSuggestionsPort> {
        RESTWordSuggestionsOutputAdapter(
            shqipRESTService = get()
        )
    }

    single<GetAlbanianTranslationOfEnglishWordPort> {
        TODO()
    }

    viewModelOf(::HomeScreenViewModel)
}