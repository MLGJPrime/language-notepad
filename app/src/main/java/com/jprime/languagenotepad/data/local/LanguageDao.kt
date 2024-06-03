package com.jprime.languagenotepad.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.jprime.languagenotepad.domain.model.Language
import kotlinx.coroutines.flow.Flow

@Dao
interface LanguageDao {

    @Upsert
    suspend fun upsert(language: Language)

    @Delete
    suspend fun delete(language: Language)

    @Query("SELECT * FROM Language")
    fun getAllLanguages(): Flow<List<Language>>

    @Query("SELECT * FROM Language WHERE id")
    suspend fun getLanguage(id: Int): Language?
}