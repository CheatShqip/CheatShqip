package com.cheatshqip.adapter.output

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entry")
data class EntryEntity(
    @PrimaryKey val rowid: Long,
    val id: String,
    val pos: String,
    @ColumnInfo(name = "albanian_headword") val albanianHeadword: String,
    @ColumnInfo(name = "albanian_ascii") val albanianAscii: String,
    @ColumnInfo(name = "stress_marked") val stressMarked: String?,
    @ColumnInfo(name = "compact_form") val compactForm: String?,
    val gender: String?,
    @ColumnInfo(name = "noun_sg_indef") val nounSgIndef: String?,
    @ColumnInfo(name = "noun_sg_def") val nounSgDef: String?,
    @ColumnInfo(name = "noun_pl_indef") val nounPlIndef: String?,
    @ColumnInfo(name = "noun_pl_def") val nounPlDef: String?,
    val english: String,
    val gloss: String,
    @ColumnInfo(name = "data_json") val dataJson: String,
    @ColumnInfo(name = "frequency") val frequency: Float = 0f,
)
