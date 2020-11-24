package com.traceon.batur.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Desa(
    val ID: String,
    val aktif: String,
    val area_ID: String,
    val created_at: String,
    val deleted_at: String,
    val kecamatan_ID: String,
    val kelurahan: String,
    val kelurahan_ID: String,
    val kode: String,
    val kota_ID: String,
    val manajemen_unit_ID: String,
    val modified_at: String
) : Parcelable