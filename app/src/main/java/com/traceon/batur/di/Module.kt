package com.traceon.batur.di

import android.content.Context
import androidx.room.Room
import com.traceon.batur.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class Module {
    @Provides
    @Singleton
    fun provideContext(context: Context): Context = context

    @Provides
    @Singleton
    fun provideCompositeDisposable() = CompositeDisposable()

    @Provides
    @DbName
    @Singleton
    fun provideDbName() = "traceon_batur.db"

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        @DbName dbName: String
    ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, dbName).build()

    @Provides
    @Singleton
    fun provideBaselineDao(appDatabase: AppDatabase) = appDatabase.baselineDao()

    @Provides
    @Singleton
    fun provideJenisKomoditasDao(appDatabase: AppDatabase) = appDatabase.komoditasDao()

    @Provides
    @Singleton
    fun provideLahanDao(appDatabase: AppDatabase) = appDatabase.lahanDao()

    @Provides
    @Singleton
    fun providePetaniDao(appDatabase: AppDatabase) = appDatabase.petaniDao()

    @Provides
    @Singleton
    fun provideReferralDao(appDatabase: AppDatabase) = appDatabase.referralDao()

    @Provides
    @Singleton
    fun provideSatuanDao(appDatabase: AppDatabase) = appDatabase.satuanDao()

    @Provides
    @Singleton
    fun provideVisitDao(appDatabase: AppDatabase) = appDatabase.visitDao()
}