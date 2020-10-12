package com.traceon.batur.data.db

import androidx.room.*
import com.traceon.batur.data.model.Referral
import io.reactivex.Flowable

@Dao
interface ReferralDao {
    @Query("SELECT * FROM referrals ORDER BY id ASC")
    fun getAll(): Flowable<List<Referral>>

    @Query("SELECT * FROM referrals where id = :id")
    fun getById(id: String): Flowable<Referral>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertALl(referral: List<Referral>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(referral: Referral)

    @Update
    fun update(referral: Referral)

    @Delete
    fun delete(referral: Referral)
}