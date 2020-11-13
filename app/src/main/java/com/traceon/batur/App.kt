package com.traceon.batur

import android.app.Application
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.traceon.batur.data.db.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        Realm.deleteRealm(Realm.getDefaultConfiguration() ?: return)
        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .allowQueriesOnUiThread(true)
                .build()
        )
        AppDatabase.getInstance()
        FirebaseCrashlytics.getInstance()
        FirebaseAnalytics.getInstance(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Thread {
            Glide.get(this).clearDiskCache()
            Glide.get(this).clearMemory()
        }.start()
    }
}