package com.cheatshqip.adapter.output

import com.cheatshqip.application.port.output.GetAlbanianTranslationOfEnglishWordPort
import com.cheatshqip.domain.Word
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first

class MlKitTranslator: GetAlbanianTranslationOfEnglishWordPort {
    override suspend fun getAlbanianTranslationOfEnglishWord(englishWord: Word): Word {
        return translate(englishWord)
            .first()
    }

    private fun translate(englishWord: Word) = callbackFlow<Word> {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.ALBANIAN)
            .build()
        val englishAlbanianTranslator = Translation.getClient(options)

        var conditions = DownloadConditions.Builder()
            .build()

        englishAlbanianTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                englishAlbanianTranslator.translate(englishWord.value)
                    .addOnSuccessListener { translatedText ->
                        trySend(Word(translatedText))
                    }
                    .addOnFailureListener { exception ->
                        throw (exception)
                    }
            }
            .addOnFailureListener { exception ->
                throw (exception)
            }
        awaitClose {
            // does nothing as we can't cancel the mlkit tasks
        }
    }
}