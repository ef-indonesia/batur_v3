package com.traceon.batur.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "visities")
data class Visit(
    @SerializedName("baseline_data_ID")
    @ColumnInfo(name = "baseline_data_ID")
    val baseline_data_ID: String? = null,
    @SerializedName("desa")
    @ColumnInfo(name = "desa")
    val desa: String? = null,
    @SerializedName("desa_ID")
    @ColumnInfo(name = "desa_ID")
    val desa_ID: String? = null,
    @SerializedName("group_komoditas_ID")
    @ColumnInfo(name = "group_komoditas_ID")
    val group_komoditas_ID: String? = null,
    @SerializedName("kode_lahan")
    @ColumnInfo(name = "kode_lahan")
    val kode_lahan: String? = null,
    @SerializedName("komoditas")
    @ColumnInfo(name = "komoditas")
    val komoditas: String? = null,
    @SerializedName("lahan_ID")
    @ColumnInfo(name = "lahan_ID")
    val lahan_ID: String? = null,
    @SerializedName("petani")
    @ColumnInfo(name = "petani")
    val petani: String? = null,
    @SerializedName("petani_ID")
    @ColumnInfo(name = "petani_ID")
    val petani_ID: String? = null,
    @SerializedName("program_visit_ID")
    @ColumnInfo(name = "program_visit_ID")
    val program_visit_ID: String? = null,
    @ColumnInfo(name = "no_urut")
    var no_urut: Int = 0,
    @ColumnInfo(name = "warna")
    var warna: String? = null,
    @PrimaryKey(autoGenerate = true)
    @SerializedName("ID")
    @ColumnInfo(name = "ID")
    var id: Long

) : Parcelable