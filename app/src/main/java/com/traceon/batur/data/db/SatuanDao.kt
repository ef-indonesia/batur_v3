package com.traceon.batur.data.db

import androidx.room.*
import com.traceon.batur.data.model.Satuan
import io.reactivex.Flowable

@Dao
interface SatuanDao {
    @Query("SELECT * FROM satuans ORDER BY id ASC")
    fun getAll(): Flowable<List<Satuan>>

    @Query("SELECT * FROM satuans where id = :id")
    fun getById(id: String): Flowable<Satuan>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertALl(satuan: List<Satuan>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(satuan: Satuan)

    @Update
    fun update(satuan: Satuan)

    @Delete
    fun delete(satuan: Satuan)
}