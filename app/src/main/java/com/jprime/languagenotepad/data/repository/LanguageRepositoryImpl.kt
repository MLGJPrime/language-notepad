package com.jprime.languagenotepad.data.repository

import com.jprime.languagenotepad.data.local.LanguageDao
import com.jprime.languagenotepad.domain.repository.LanguageRepository
import kotlinx.coroutines.flow.first

class LanguageRepositoryImpl(private val languageDao: LanguageDao) : LanguageRepository {
    override suspend fun hasLanguages(): Boolean {
        return languageDao.getAllLanguages().first().isNotEmpty()
    }
}