package com.traceon.batur.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "referrals")
data class Referral(
    @SerializedName("desa_ID")
    @ColumnInfo(name = "desa_ID")
    var desaID: Int? = null,

    @SerializedName("desa")
    @ColumnInfo(name = "desa")
    var desa: String? = null,

    @SerializedName("kode_referal")
    @ColumnInfo(name = "kode_referal")
    var kodeReferal: String? = null,

    @SerializedName("penetapan_desa_ID")
    @ColumnInfo(name = "penetapan_desa_ID")
    var penetapanDesaID: Int? = null,

    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null,

    @SerializedName("area_ID")
    @ColumnInfo(name = "area_ID")
    var areaID: Int? = null,

    @SerializedName("deleted_at")
    @ColumnInfo(name = "deleted_at")
    var deletedAt: String? = null,

    @SerializedName("aktif")
    @ColumnInfo(name = "aktif")
    var aktif: String? = null,

    @SerializedName("masa_aktif")
    @ColumnInfo(name = "masa_aktif")
    var masaAktif: String? = null,

    @SerializedName("manajemen_unit_ID")
    @ColumnInfo(name = "manajemen_unit_ID")
    var manajemenUnitID: Int? = null,

    @SerializedName("staff_ID")
    @ColumnInfo(name = "staff_ID")
    var staffID: Int? = null,

    @SerializedName("modified_at")
    @ColumnInfo(name = "modified_at")
    var modifiedAt: String? = null,

    @SerializedName("sisa_hari")
    @ColumnInfo(name = "sisa_hari")
    var sisaHari: Int? = null,

    @PrimaryKey
    @SerializedName("ID")
    @ColumnInfo(name = "ID")
    var iD: Int? = null

) : Parcelable