package com.traceon.batur.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VisitDetail(
    val baseline_data_ID: String,
    val desa: String,
    val desa_ID: String,
    val group_komoditas_ID: String,
    val kode_lahan: String,
    val komoditas: String,
    val lahan_ID: String,
    val petani: String,
    val petani_ID: String
) : Parcelable