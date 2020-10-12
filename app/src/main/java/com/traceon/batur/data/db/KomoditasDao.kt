package com.traceon.batur.data.db

import androidx.room.*
import com.traceon.batur.data.model.Komoditas
import io.reactivex.Flowable

@Dao
interface KomoditasDao {
    @Query("SELECT * FROM komoditases ORDER BY id ASC")
    fun getAll(): Flowable<List<Komoditas>>

    @Query("SELECT * FROM komoditases where id = :id")
    fun getById(id: String): Flowable<Komoditas>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertALl(komoditas: List<Komoditas>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(komoditas: Komoditas)

    @Update
    fun update(komoditas: Komoditas)

    @Delete
    fun delete(komoditas: Komoditas)
}
