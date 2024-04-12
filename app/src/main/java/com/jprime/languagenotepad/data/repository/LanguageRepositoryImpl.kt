package com.jprime.languagenotepad.data.repository

import com.jprime.languagenotepad.domain.repository.LanguageRepository

class LanguageRepositoryImpl : LanguageRepository {
    override suspend fun hasLanguages(): Boolean {
        return true
    }
}