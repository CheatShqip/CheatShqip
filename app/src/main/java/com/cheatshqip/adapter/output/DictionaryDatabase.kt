package com.cheatshqip.adapter.output

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [EntryEntity::class, EntryEnglishEntity::class],
    version = 3,
    exportSchema = true,
)
abstract class DictionaryDatabase : RoomDatabase() {
    abstract fun dictionaryDao(): DictionaryDao
}
