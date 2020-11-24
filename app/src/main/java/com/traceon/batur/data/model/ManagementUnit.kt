package com.traceon.batur.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ManagementUnit(
    val ID: String,
    val aktif: String,
    val created_at: String,
    val deleted_at: String,
    val kode: String,
    val modified_at: String,
    val nama: String
) : Parcelable