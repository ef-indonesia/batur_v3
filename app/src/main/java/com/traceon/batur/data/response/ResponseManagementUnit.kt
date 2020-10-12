package com.traceon.batur.data.response

import com.google.gson.annotations.SerializedName

data class ResponseManagementUnit(
    @SerializedName("nama")
    val nama: String? = null,

    @SerializedName("kode")
    val kode: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("modified_at")
    val modifiedAt: String? = null,

    @SerializedName("deleted_at")
    val deletedAt: String? = null,

    @SerializedName("aktif")
    val aktif: String? = null,

    @SerializedName("ID")
    val iD: String? = null
)