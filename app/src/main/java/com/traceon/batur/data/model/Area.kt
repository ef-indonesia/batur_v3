package com.traceon.batur.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Area(
    val ID: String,
    val aktif: String,
    val created_at: String,
    val deleted_at: String,
    val kode: String,
    val manajemen_unit: String,
    val manajemen_unit_ID: String,
    val modified_at: String,
    val nama: String
) : Parcelable