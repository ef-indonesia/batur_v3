package com.traceon.batur.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "baselines")
data class Baseline(
    @SerializedName("group_komoditas_ID")
    @ColumnInfo(name = "group_komoditas_ID")
    var komoditasID: Int? = 0,

    @SerializedName("ras")
    @ColumnInfo(name = "ras")
    var ras: String? = "",

    @SerializedName("nilai")
    @ColumnInfo(name = "nilai")
    var nilai: String? = "",

    @SerializedName("author")
    @ColumnInfo(name = "author")
    var author: String? = "",

    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    var createdAt: String? = "",

    @SerializedName("sub_lahan_ID")
    @ColumnInfo(name = "sub_lahan_ID")
    var subLahanID: String? = "",

    @SerializedName("deleted_at")
    @ColumnInfo(name = "deleted_at")
    var deletedAt: String? = "",

    @SerializedName("aktif")
    @ColumnInfo(name = "aktif")
    var aktif: String? = "",

    @SerializedName("mekanisme")
    @ColumnInfo(name = "mekanisme")
    var mekanisme: String? = "",

    @SerializedName("lock")
    @ColumnInfo(name = "lock")
    var lock: String? = "",

    @SerializedName("produktifitas")
    @ColumnInfo(name = "produktifitas")
    var produktifitas: String? = "",

    @SerializedName("komoditas")
    @ColumnInfo(name = "komoditas")
    var komoditas: String? = "",

    @SerializedName("luas_pemanfaatan")
    @ColumnInfo(name = "luas_pemanfaatan")
    var luasPemanfaatan: String? = "",

    @SerializedName("modified_at")
    @ColumnInfo(name = "modified_at")
    var modifiedAt: String? = "",

    @SerializedName("pemanfaatan")
    @ColumnInfo(name = "pemanfaatan")
    var pemanfaatan: String? = "",

    @SerializedName("gps")
    @ColumnInfo(name = "gps")
    var gps: String? = "",

    @SerializedName("kode_lahan")
    @ColumnInfo(name = "kode_lahan")
    var kodeLahan: String? = "",

    @SerializedName("lahan_ID")
    @ColumnInfo(name = "lahan_ID")
    var lahanID: Int? = 0,

    @SerializedName("petani_ID")
    @ColumnInfo(name = "petani_ID")
    var petaniID: Int? = 0,

    @SerializedName("foto_baseline")
    @ColumnInfo(name = "foto_baseline")
    var fotoBaseline: String? = "",

    @SerializedName("uri_foto")
    @ColumnInfo(name = "uri_foto")
    var uriFoto: String? = "",

    @SerializedName("status")
    @ColumnInfo(name = "status")
    var status: String? = "",

    @PrimaryKey
    @SerializedName("ID")
    @ColumnInfo(name = "ID")
    var iD: Int? = 0
) : Parcelable