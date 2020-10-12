package com.traceon.batur.data.response

import com.google.gson.annotations.SerializedName

data class ResponseUpdate(
    @SerializedName("kode_petani")
    val kodePetani: String? = null,

    @SerializedName("kode_lahan")
    val kodeLahan: String? = null,

    @SerializedName("response")
    val response: String? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("status")
    val status: Boolean? = null,

    @SerializedName("ID")
    val id: String? = null
)