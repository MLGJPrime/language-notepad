package com.jprime.languagenotepad.domain.usecases.app_entry

import com.jprime.languagenotepad.domain.repository.LanguageRepository

class AppEntryUseCases(private val languageRepository: LanguageRepository) {
    suspend fun shouldStartFromHomeScreen(): Boolean {
        return languageRepository.hasLanguages()
    }

}