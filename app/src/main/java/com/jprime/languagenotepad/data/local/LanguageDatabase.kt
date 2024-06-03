package com.jprime.languagenotepad.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jprime.languagenotepad.domain.model.Language

@Database(entities = [Language::class], version = 1)
abstract class LanguageDatabase : RoomDatabase() {
    abstract fun languageDao(): LanguageDao
}