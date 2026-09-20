package com.cheatshqip.di

import com.cheatshqip.DeclineScreenViewModel
import com.cheatshqip.HomeScreenViewModel
import com.cheatshqip.WordDetailViewModel
import com.cheatshqip.adapter.output.DictionaryDatabase
import com.cheatshqip.adapter.output.RoomAlbanianWordDetailOutputAdapter
import com.cheatshqip.adapter.output.SqliteEnglishToAlbanianOutputAdapter
import com.cheatshqip.adapter.output.createDictionaryDatabase
import com.cheatshqip.application.AlbanianWordService
import com.cheatshqip.application.TranslationService
import com.cheatshqip.application.port.input.GetAlbanianWordDetailUseCase
import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.application.port.output.GetAlbanianWordDetailPort
import com.cheatshqip.application.port.output.GetEnglishToAlbanianTranslationsPort
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {
    single<CoroutineDispatcher> {
        Dispatchers.IO
    }

    single<GetWordTranslationSuggestionsUseCase> {
        TranslationService(
            getEnglishToAlbanianTranslationsPort = get()
        )
    }

    single<DictionaryDatabase> {
        createDictionaryDatabase(androidContext())
    }

    single { get<DictionaryDatabase>().dictionaryDao() }

    single<GetEnglishToAlbanianTranslationsPort> {
        SqliteEnglishToAlbanianOutputAdapter(dao = get())
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

    viewModel {
        DeclineScreenViewModel(
            getAlbanianWordDetailUseCase = get(),
            coroutineDispatcher = get(),
        )
    }
}
