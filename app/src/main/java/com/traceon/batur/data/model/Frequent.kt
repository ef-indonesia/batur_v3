package com.traceon.batur.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Frequent(
    val last_visit: String? = null,
    val total_frekuensi_0: Int? = null,
    val total_frekuensi_1: Int? = null,
    val total_frekuensi_2: Int? = null,
    val total_frekuensi_3: Int? = null,
    val total_frekuensi_4: Int? = null,
    val total_frekuensi_5: Int? = null
) : Parcelable