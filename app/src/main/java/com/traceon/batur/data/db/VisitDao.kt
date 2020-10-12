package com.traceon.batur.data.db

import androidx.room.*
import com.traceon.batur.data.model.Visit
import io.reactivex.Flowable

@Dao
interface VisitDao {
    @Query("SELECT * FROM visities ORDER BY id ASC")
    fun getAll(): Flowable<List<Visit>>

    @Query("SELECT * FROM visities where id = :id")
    fun getById(id: String): Flowable<Visit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertALl(visit: List<Visit>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(visit: Visit)

    @Update
    fun update(visit: Visit)

    @Delete
    fun delete(visit: Visit)
}