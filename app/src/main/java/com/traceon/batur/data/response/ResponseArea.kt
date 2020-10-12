package com.traceon.batur.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseArea(
    @SerializedName("nama")
    val nama: String? = null,

    @SerializedName("manajemen_unit_ID")
    val manajemenUnitID: Int? = null,

    @SerializedName("kode")
    val kode: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("manajemen_unit")
    val manajemenUnit: String? = null,

    @SerializedName("ID")
    val iD: Int? = null,

    @SerializedName("modified_at")
    val modifiedAt: String? = null,

    @SerializedName("deleted_at")
    val deletedAt: String? = null,

    @SerializedName("aktif")
    val aktif: String? = null
): Parcelable