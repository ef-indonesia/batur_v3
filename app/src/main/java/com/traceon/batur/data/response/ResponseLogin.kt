package com.traceon.batur.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseLogin(
    val API_KEY: String,
    val ID: Int,
    val area: String,
    val area_ID: Int,
    val client_name: String,
    val database: String,
    val desa: String,
    val desa_ID: Int,
    val foto: String,
    val kode_perusahaan: String,
    val manajemen_unit: String,
    val manajemen_unit_ID: Int,
    val message: String,
    val nama_lengkap: String,
    val result: Boolean,
    val status: String,
    val table_ID: Int,
    val table_name: String,
    val timeout: Int,
    val tipe_user: String,
    val token: String,
    val user_ID: Int
) : Parcelable