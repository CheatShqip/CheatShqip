package com.cheatshqip.adapter.output

import com.cheatshqip.application.port.output.GetAlbanianTranslationOfEnglishWordPort
import com.cheatshqip.domain.Word
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MlKitTranslator : GetAlbanianTranslationOfEnglishWordPort {
    override suspend fun getAlbanianTranslationOfEnglishWord(englishWord: Word): Word {
        return translate(englishWord)
    }

    private suspend fun translate(englishWord: Word): Word = suspendCancellableCoroutine { continuation ->
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.ALBANIAN)
            .build()
        val englishAlbanianTranslator = Translation.getClient(options)

        continuation.invokeOnCancellation {
            englishAlbanianTranslator.close()
        }

        val conditions = DownloadConditions.Builder().build()

        englishAlbanianTranslator
            .downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                englishAlbanianTranslator.translate(englishWord.value)
                    .addOnSuccessListener { translatedText ->
                        continuation.resume(Word(translatedText))
                    }
                    .addOnFailureListener(continuation::resumeWithException)
            }
            .addOnFailureListener(continuation::resumeWithException)
    }
}
