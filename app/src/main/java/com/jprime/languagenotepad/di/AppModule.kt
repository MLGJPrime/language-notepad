package com.jprime.languagenotepad.di

import com.jprime.languagenotepad.data.repository.LanguageRepositoryImpl
import com.jprime.languagenotepad.domain.repository.LanguageRepository
import com.jprime.languagenotepad.domain.usecases.app_entry.AppEntryUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLanguageRepository(): LanguageRepository = LanguageRepositoryImpl()

    @Provides
    @Singleton
    fun provideAppEntryUseCases(languageRepository: LanguageRepository): AppEntryUseCases =
        AppEntryUseCases(languageRepository)
}