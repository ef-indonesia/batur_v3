package com.traceon.batur.data.db

import androidx.room.*
import com.traceon.batur.data.model.Lahan
import io.reactivex.Flowable

@Dao
interface LahanDao {
    @Query("SELECT * FROM lahans ORDER BY id ASC")
    fun getAll(): Flowable<List<Lahan>>

    @Query("SELECT * FROM lahans where id = :id")
    fun getById(id: String): Flowable<Lahan>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertALl(lahan: List<Lahan>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(lahan: Lahan)

    @Update
    fun update(lahan: Lahan)

    @Delete
    fun delete(lahan: Lahan)
}