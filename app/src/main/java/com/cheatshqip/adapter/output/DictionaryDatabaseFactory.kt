package com.cheatshqip.adapter.output

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

private const val DICTIONARY_ASSET_NAME = "dictionary.db"

fun createDictionaryDatabase(context: Context, databaseName: String = DICTIONARY_ASSET_NAME): DictionaryDatabase {
    copyDictionaryAssetIfMissing(context, databaseName)
    syncUserVersion(context, databaseName)
    return Room.databaseBuilder(context, DictionaryDatabase::class.java, databaseName)
        .setDriver(BundledSQLiteDriver())
        .build()
}

private fun copyDictionaryAssetIfMissing(context: Context, databaseName: String) {
    val destination = context.getDatabasePath(databaseName)
    if (destination.exists() && hasCurrentSchema(destination)) return
    destination.parentFile?.mkdirs()
    context.assets.open(DICTIONARY_ASSET_NAME).use { input ->
        destination.outputStream().use { output -> input.copyTo(output) }
    }
}

private fun hasCurrentSchema(destination: File): Boolean =
    runCatching {
        BundledSQLiteDriver().open(destination.absolutePath).use { connection ->
            connection.prepare("SELECT 1 FROM entry_english LIMIT 1").use { statement ->
                statement.step()
            }
        }
        true
    }.getOrDefault(false)

private fun syncUserVersion(context: Context, databaseName: String) {
    val destination = context.getDatabasePath(databaseName)
    runCatching {
        BundledSQLiteDriver().open(destination.absolutePath).use { connection ->
            connection.prepare("PRAGMA user_version = 3").use { statement ->
                statement.step()
            }
        }
    }
}
