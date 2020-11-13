package com.traceon.batur.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.disposables.CompositeDisposable
import io.realm.Realm
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class Module {
    @Provides
    @Singleton
    internal fun provideContext(context: Context): Context = context

    @Provides
    @Singleton
    internal fun provideCompositeDisposable() = CompositeDisposable()

    @Provides
    @Singleton
    internal fun provideRealm(): Realm {
        return Realm.getDefaultInstance()
    }

}