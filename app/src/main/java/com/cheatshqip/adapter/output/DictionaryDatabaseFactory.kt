package com.cheatshqip.adapter.output

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

private const val DICTIONARY_ASSET_NAME = "dictionary.db"

// Keep in sync with _ASSET_REVISION in words/src/dict_builder/io/sqlite_writer.py.
private const val DICTIONARY_ASSET_REVISION = "3"

fun createDictionaryDatabase(context: Context, databaseName: String = DICTIONARY_ASSET_NAME): DictionaryDatabase {
    refreshDictionaryAssetIfNeeded(context, databaseName)
    syncUserVersion(context, databaseName)
    return Room.databaseBuilder(context, DictionaryDatabase::class.java, databaseName)
        .setDriver(BundledSQLiteDriver())
        .build()
}

private fun refreshDictionaryAssetIfNeeded(context: Context, databaseName: String) {
    val destination = context.getDatabasePath(databaseName)
    val revisionFile = File(destination.parentFile, "$databaseName.revision")
    val installedRevision = runCatching { revisionFile.readText().trim() }.getOrNull()
    if (
        destination.exists() &&
        hasCurrentSchema(destination) &&
        installedRevision == DICTIONARY_ASSET_REVISION
    ) {
        return
    }
    context.deleteDatabase(databaseName)
    destination.parentFile?.mkdirs()
    context.assets.open(DICTIONARY_ASSET_NAME).use { input ->
        destination.outputStream().use { output -> input.copyTo(output) }
    }
    revisionFile.writeText(DICTIONARY_ASSET_REVISION)
}

private fun hasCurrentSchema(destination: File): Boolean =
    runCatching {
        BundledSQLiteDriver().open(destination.absolutePath).use { connection ->
            connection.prepare("SELECT 1 FROM entry_english LIMIT 1").use { statement ->
                statement.step()
            }
            connection.prepare("SELECT 1 FROM english_search LIMIT 1").use { statement ->
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
