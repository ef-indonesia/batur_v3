package com.traceon.batur.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "petanies")
data class Petani(
    @SerializedName("area")
    @ColumnInfo(name = "area")
    var area: String? = null,

    @SerializedName("desa_ID")
    @ColumnInfo(name = "desa_ID")
    var desaID: String? = null,

    @SerializedName("ukuran_total")
    @ColumnInfo(name = "ukuran_total")
    var ukuranTotal: Double? = null,

    @SerializedName("kode_pos")
    @ColumnInfo(name = "kode_pos")
    var kodePos: String? = null,

    @SerializedName("foto_petani")
    @ColumnInfo(name = "foto_petani")
    var fotoPetani: String? = null,

    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null,

    @SerializedName("lahan_total")
    @ColumnInfo(name = "lahan_total")
    var lahanTotal: Int? = null,

    @SerializedName("foto_ktp")
    @ColumnInfo(name = "foto_ktp")
    var fotoKtp: String? = "",

    @SerializedName("ukuran_text")
    @ColumnInfo(name = "ukuran_text")
    var ukuranText: Double? = null,

    @SerializedName("area_ID")
    @ColumnInfo(name = "area_ID")
    var areaID: String? = null,

    @SerializedName("nomor")
    @ColumnInfo(name = "nomor")
    var nomor: String? = null,

    @SerializedName("deleted_at")
    @ColumnInfo(name = "deleted_at")
    var deletedAt: String? = null,

    @SerializedName("aktif")
    @ColumnInfo(name = "aktif")
    var aktif: String? = null,

    @SerializedName("alamat")
    @ColumnInfo(name = "alamat")
    var alamat: String? = null,

    @SerializedName("nama")
    @ColumnInfo(name = "nama")
    var nama: String? = null,

    @SerializedName("manajemen_unit_ID")
    @ColumnInfo(name = "manajemen_unit_ID")
    var manajemenUnitID: String? = null,

    @SerializedName("no_ktp")
    @ColumnInfo(name = "no_ktp")
    var noKtp: String? = null,

    @SerializedName("kode")
    @ColumnInfo(name = "kode")
    var kode: String? = null,

    @SerializedName("lahan_kunci")
    @ColumnInfo(name = "lahan_kunci")
    var lahanKunci: String? = null,

    @SerializedName("manajemen_unit")
    @ColumnInfo(name = "manajemen_unit")
    var manajemenUnit: String? = null,

    @SerializedName("no_telp")
    @ColumnInfo(name = "no_telp")
    var noTelp: String? = null,

    @SerializedName("modified_at")
    @ColumnInfo(name = "modified_at")
    var modifiedAt: String? = null,

    @SerializedName("email")
    @ColumnInfo(name = "email")
    var email: String? = null,

    @SerializedName("uriFoto")
    @ColumnInfo(name = "uriFoto")
    var uriFoto: String? = "",

    @SerializedName("status")
    @ColumnInfo(name = "status")
    var status: String? = null,

    @PrimaryKey
    @SerializedName("ID")
    @ColumnInfo(name = "ID")
    var iD: Int? = null

) : Parcelable