package com.jprime.languagenotepad.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "word_pairs",
    foreignKeys = [ForeignKey(
        entity = WordClass::class,
        parentColumns = ["id"],
        childColumns = ["classId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("classId")]
)
data class WordPair(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val classId: Long,
    val word: String = "",
    val translation: String = "",
)
