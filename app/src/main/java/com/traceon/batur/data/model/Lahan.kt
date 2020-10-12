package com.traceon.batur.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "lahans")
data class Lahan(
    @ColumnInfo(name = "luas_lahan")
    @SerializedName("luas_lahan")
    var luasLahan: String? = null,

    @ColumnInfo(name = "petani_ID")
    @SerializedName("petani_ID")
    var petaniID: String? = null,

    @ColumnInfo(name = "luas_ditanami")
    @SerializedName("luas_ditanami")
    var luasDitanami: String? = null,

    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    var createdAt: String? = null,

    @ColumnInfo(name = "gps")
    @SerializedName("gps")
    var gps: String? = null,

    @ColumnInfo(name = "deleted_at")
    @SerializedName("deleted_at")
    var deletedAt: String? = null,

    @ColumnInfo(name = "nomor")
    @SerializedName("nomor")
    var nomor: String? = null,

    @ColumnInfo(name = "aktif")
    @SerializedName("aktif")
    var aktif: String? = null,

    @ColumnInfo(name = "kode")
    @SerializedName("kode")
    var kode: String? = null,

    @ColumnInfo(name = "lock")
    @SerializedName("lock")
    var lock: String? = null,

    @ColumnInfo(name = "petani")
    @SerializedName("petani")
    var petani: String? = null,

    @ColumnInfo(name = "modified_at")
    @SerializedName("modified_at")
    var modifiedAt: String? = null,

    @ColumnInfo(name = "no_dokumen")
    @SerializedName("no_dokumen")
    var noDokumen: String? = null,

    @ColumnInfo(name = "deskripsi")
    @SerializedName("deskripsi")
    var deskripsi: String? = null,

    @ColumnInfo(name = "foto_lahan")
    @SerializedName("foto_lahan")
    var fotoLahan: String? = null,

    @ColumnInfo(name = "uri_foto")
    @SerializedName("uri_foto")
    var uriFoto: String? = null,

    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: String? = null,

    @PrimaryKey
    @ColumnInfo(name = "ID")
    @SerializedName("ID")
    var iD: Int? = null

) : Parcelable