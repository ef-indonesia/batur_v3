package com.traceon.batur.data.db

import androidx.room.*
import com.traceon.batur.data.model.Petani
import io.reactivex.Flowable

@Dao
interface PetaniDao {
    @Query("SELECT * FROM petanies ORDER BY id ASC")
    fun getAll(): Flowable<List<Petani>>

    @Query("SELECT * FROM petanies where id = :id")
    fun getById(id: String): Flowable<Petani>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertALl(petani: List<Petani>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(petani: Petani)

    @Update
    fun update(petani: Petani)

    @Delete
    fun delete(petani: Petani)
}