package com.jprime.languagenotepad.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_classes")
data class WordClass(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val className: String? = null,
    val classTranslation: String? = null,
)
