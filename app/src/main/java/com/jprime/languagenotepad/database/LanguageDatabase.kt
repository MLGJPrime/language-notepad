package com.jprime.languagenotepad.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jprime.languagenotepad.dao.WordClassDao
import com.jprime.languagenotepad.dao.WordPairDao
import com.jprime.languagenotepad.entities.WordClass
import com.jprime.languagenotepad.entities.WordPair

@Database(entities = [WordClass::class, WordPair::class], version = 1, exportSchema = false)
abstract class LanguageDatabase : RoomDatabase() {

    abstract fun wordClassDao(): WordClassDao
    abstract fun wordPairDao(): WordPairDao

    companion object {
        @Volatile
        private var INSTANCE: LanguageDatabase? = null

        fun getDatabase(context: Context): LanguageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LanguageDatabase::class.java,
                    "language.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}