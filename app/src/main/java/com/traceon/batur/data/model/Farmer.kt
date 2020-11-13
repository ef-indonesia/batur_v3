package com.traceon.batur.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Farmer(
    var idPetani: String,
    var namaPetani: String,
    var alamatPetani: String,
    var ukuranText: Double?,
    var ukuranTotal: Double?,
    var dipilih: Boolean,
    var foto: Boolean,
    var lahanTotal: String,
    var lahanKunci: String
) : Parcelable