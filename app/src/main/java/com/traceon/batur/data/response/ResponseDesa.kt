package com.traceon.batur.data.response

import com.google.gson.annotations.SerializedName

data class ResponseDesa(
    @field:SerializedName("tahun")
    val tahun: String? = null,

    @field:SerializedName("k1")
    val k1: String? = null,

    @field:SerializedName("k2")
    val k2: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("area_ID")
    val areaID: String? = null,

    @field:SerializedName("kecamatan_ID")
    val kecamatanID: String? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: String? = null,

    @field:SerializedName("aktif")
    val aktif: String? = null,

    @field:SerializedName("kelurahan")
    val kelurahan: String? = null,

    @field:SerializedName("kode_baru")
    val kodeBaru: String? = null,

    @field:SerializedName("manajemen_unit_ID")
    val manajemenUnitID: String? = null,

    @field:SerializedName("kelurahan_ID")
    val kelurahanID: String? = null,

    @field:SerializedName("kode")
    val kode: String? = null,

    @field:SerializedName("ID")
    val iD: Int? = null,

    @field:SerializedName("modified_at")
    val modifiedAt: String? = null,

    @field:SerializedName("kota_ID")
    val kotaID: String? = null
)