package com.jprime.languagenotepad.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jprime.languagenotepad.entities.WordPair

@Dao
interface WordPairDao {

    @Upsert
    suspend fun upsertWordPair(wordPair: WordPair): Long

    @Query("DELETE FROM word_pairs WHERE id = :id")
    suspend fun deleteWordPair(id: Long)

    @Query("SELECT * FROM word_pairs")
    suspend fun getAllWordPairs(): List<WordPair>

    @Query("SELECT * FROM word_pairs WHERE classId = :classId")
    suspend fun getWordPairsByClassId(classId: Long): List<WordPair>

    @Query("SELECT * FROM word_pairs WHERE id = :id")
    suspend fun getWordPairById(id: Long): WordPair?
}