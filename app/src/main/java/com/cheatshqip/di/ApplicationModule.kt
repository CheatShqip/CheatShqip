package com.cheatshqip.di

import com.cheatshqip.HomeScreenViewModel
import com.cheatshqip.application.TranslationService
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val applicationModule = module {
    single { TranslationService(get()) }
    viewModelOf(::HomeScreenViewModel)
}