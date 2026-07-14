package com.cheatshqip.di

import com.cheatshqip.BuildConfig
import com.cheatshqip.HomeScreenViewModel
import com.cheatshqip.WordDetailViewModel
import com.cheatshqip.adapter.output.ApiBaseURL
import com.cheatshqip.adapter.output.DictionaryDatabase
import com.cheatshqip.adapter.output.RoomAlbanianWordDetailOutputAdapter
import com.cheatshqip.adapter.output.ShqipRESTService
import com.cheatshqip.adapter.output.SqliteWordSuggestionsOutputAdapter
import com.cheatshqip.adapter.output.createDictionaryDatabase
import com.cheatshqip.application.AlbanianWordService
import com.cheatshqip.application.TranslationService
import com.cheatshqip.application.port.input.GetAlbanianWordDetailUseCase
import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.application.port.output.GetAlbanianWordDetailPort
import com.cheatshqip.application.port.output.GetWordSuggestionsPort
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val HTTP_APPLICATION_JSON = "application/json"

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

    single<DictionaryDatabase> {
        createDictionaryDatabase(androidContext())
    }

    single { get<DictionaryDatabase>().dictionaryDao() }

    single<GetWordSuggestionsPort> {
        SqliteWordSuggestionsOutputAdapter(dao = get())
    }

    single<GetAlbanianWordDetailPort> {
        RoomAlbanianWordDetailOutputAdapter(dao = get())
    }

    single<GetAlbanianWordDetailUseCase> {
        AlbanianWordService(getAlbanianWordDetailPort = get())
    }

    viewModel {
        HomeScreenViewModel(
            getWordTranslationSuggestionsUseCase = get(),
            coroutineDispatcher = get()
        )
    }

    viewModel {
        WordDetailViewModel(
            savedStateHandle = get(),
            getAlbanianWordDetailUseCase = get(),
            coroutineDispatcher = get(),
        )
    }
}

private val jsonBuilder = Json {
    ignoreUnknownKeys = true
    isLenient = true
}
