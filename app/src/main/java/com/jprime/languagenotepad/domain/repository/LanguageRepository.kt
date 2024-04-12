package com.jprime.languagenotepad.domain.repository

interface LanguageRepository {
    suspend fun hasLanguages(): Boolean
}