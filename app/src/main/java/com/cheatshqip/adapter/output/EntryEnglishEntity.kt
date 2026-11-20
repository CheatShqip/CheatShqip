package com.cheatshqip.adapter.output

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entry_english")
data class EntryEnglishEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "entry_rowid") val entryRowid: Long,
    val english: String,
    val position: Int,
)
