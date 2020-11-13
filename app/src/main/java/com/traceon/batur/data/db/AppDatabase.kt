package com.traceon.batur.data.db

import com.traceon.batur.data.model.*
import com.traceon.batur.utils.AppConstant
import com.traceon.batur.utils.Helper
import com.vicpin.krealmextensions.RealmConfigStore
import io.realm.Realm

object AppDatabase {
    fun getInstance() {
        Realm.deleteRealm(Helper.setRealmConfig(AppConstant.DB_PETANI))
        Realm.deleteRealm(Helper.setRealmConfig(AppConstant.DB_REFERRAL))
        Realm.deleteRealm(Helper.setRealmConfig(AppConstant.DB_LAHAN))
        Realm.deleteRealm(Helper.setRealmConfig(AppConstant.DB_SATUAN))
        Realm.deleteRealm(Helper.setRealmConfig(AppConstant.DB_KOMODITAS))

        RealmConfigStore.initModule(
            Petani::class.java,
            Helper.setRealmConfig(AppConstant.DB_PETANI)
        )
        RealmConfigStore.initModule(
            Referral::class.java, Helper.setRealmConfig(AppConstant.DB_REFERRAL)
        )
        RealmConfigStore.initModule(
            Lahan::class.java, Helper.setRealmConfig(AppConstant.DB_LAHAN)
        )
        RealmConfigStore.initModule(
            Satuan::class.java, Helper.setRealmConfig(AppConstant.DB_SATUAN)
        )
        RealmConfigStore.initModule(
            Komoditas::class.java, Helper.setRealmConfig(AppConstant.DB_KOMODITAS)
        )
    }
}