package com.cheatshqip.di

import com.cheatshqip.application.TranslationService
import org.koin.dsl.module

val applicationModule = module {
    single { TranslationService(get()) }
}