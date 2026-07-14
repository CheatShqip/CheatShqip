package com.cheatshqip.adapter.output

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [EntryEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class DictionaryDatabase : RoomDatabase() {
    abstract fun dictionaryDao(): DictionaryDao
}
