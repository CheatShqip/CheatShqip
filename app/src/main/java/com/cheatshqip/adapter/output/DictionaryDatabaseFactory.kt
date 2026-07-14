package com.cheatshqip.adapter.output

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

private const val DICTIONARY_ASSET_NAME = "dictionary.db"

fun createDictionaryDatabase(context: Context, databaseName: String = DICTIONARY_ASSET_NAME): DictionaryDatabase {
    copyDictionaryAssetIfMissing(context, databaseName)
    return Room.databaseBuilder(context, DictionaryDatabase::class.java, databaseName)
        .setDriver(BundledSQLiteDriver())
        .build()
}

private fun copyDictionaryAssetIfMissing(context: Context, databaseName: String) {
    val destination = context.getDatabasePath(databaseName)
    if (destination.exists()) return
    destination.parentFile?.mkdirs()
    context.assets.open(DICTIONARY_ASSET_NAME).use { input ->
        destination.outputStream().use { output -> input.copyTo(output) }
    }
}
