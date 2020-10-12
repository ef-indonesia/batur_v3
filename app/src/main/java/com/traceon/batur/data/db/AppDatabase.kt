package com.traceon.batur.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.traceon.batur.data.model.*

@Database(
    entities = [Baseline::class, Komoditas::class, Lahan::class, Petani::class, Referral::class, Satuan::class, Visit::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun baselineDao(): BaselineDao
    abstract fun lahanDao(): LahanDao
    abstract fun petaniDao(): PetaniDao
    abstract fun referralDao(): ReferralDao
    abstract fun satuanDao(): SatuanDao
    abstract fun komoditasDao(): KomoditasDao
    abstract fun visitDao(): VisitDao
}