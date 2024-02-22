package com.jprime.languagenotepad.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jprime.languagenotepad.entities.WordClass

@Dao
interface WordClassDao {

    @Upsert
    suspend fun upsertWordClass(wordClass: WordClass): Long

    @Query("DELETE FROM word_classes WHERE id = :wordClassId")
    suspend fun deleteWordClass(wordClassId: Long)

    @Query("SELECT * FROM word_classes")
    suspend fun getAllWordClasses(): List<WordClass>

    @Query("SELECT * FROM word_classes WHERE id = :wordClassId")
    suspend fun getWordClass(wordClassId: Long): WordClass?

}