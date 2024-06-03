package com.jprime.languagenotepad.di

import android.app.Application
import androidx.room.Room
import com.jprime.languagenotepad.data.local.LanguageDao
import com.jprime.languagenotepad.data.local.LanguageDatabase
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
    fun provideLanguageDatabase(application: Application): LanguageDatabase {
        return Room.databaseBuilder(
            application,
            LanguageDatabase::class.java,
            "language_database"
        ).build()
    }

    @Provides
    fun provideLanguageDao(database: LanguageDatabase): LanguageDao {
        return database.languageDao()
    }

    @Provides
    @Singleton
    fun provideLanguageRepository(dao: LanguageDao): LanguageRepository {
        return LanguageRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideAppEntryUseCases(languageRepository: LanguageRepository): AppEntryUseCases =
        AppEntryUseCases(languageRepository)
}