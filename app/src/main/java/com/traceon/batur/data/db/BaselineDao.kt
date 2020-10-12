package com.traceon.batur.data.db

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.traceon.batur.data.model.Baseline
import io.reactivex.Flowable

@Dao
interface BaselineDao {
    @Query("SELECT * FROM baselines ORDER BY id ASC")
    fun getAll(): Flowable<List<Baseline>>

    @Query("SELECT * FROM baselines where id = :id")
    fun getById(id: String): Flowable<Baseline>

    @Insert(onConflict = REPLACE)
    fun insertALl(baseline: List<Baseline>)

    @Insert(onConflict = REPLACE)
    fun insert(baseline: Baseline)

    @Update
    fun update(baseline: Baseline)

    @Delete
    fun delete(baseline: Baseline)
}