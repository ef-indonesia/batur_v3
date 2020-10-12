package com.traceon.batur.data.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "satuans")
data class Satuan(
    @SerializedName("pengali")
    @ColumnInfo(name = "pengali")
    var pengali: String? = null,

    @SerializedName("keterangan")
    @ColumnInfo(name = "keterangan")
    var keterangan: String? = null,

    @SerializedName("grup")
    @ColumnInfo(name = "grup")
    var grup: String? = null,

    @SerializedName("kode")
    @ColumnInfo(name = "kode")
    var kode: String? = null,

    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null,


    @SerializedName("komoditasID")
    @ColumnInfo(name = "komoditasID")
    var komoditasID: String? = null,

    @SerializedName("modified_at")
    @ColumnInfo(name = "modified_at")
    var modifiedAt: String? = null,

    @SerializedName("deleted_at")
    @ColumnInfo(name = "deleted_at")
    var deletedAt: String? = null,

    @SerializedName("aktif")
    @ColumnInfo(name = "aktif")
    var aktif: String? = null,

    @PrimaryKey
    @SerializedName("ID")
    @ColumnInfo(name = "ID")
    var id: String

) : Parcelable