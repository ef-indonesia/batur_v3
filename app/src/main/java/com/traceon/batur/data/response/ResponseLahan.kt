package com.traceon.batur.data.response

import com.google.gson.annotations.SerializedName

data class ResponseLahan(
    @SerializedName("luas_lahan")
    var luasLahan: String? = null,

    @SerializedName("petani_ID")
    var petaniID: String? = null,

    @SerializedName("luas_ditanami")
    var luasDitanami: String? = null,

    @SerializedName("created_at")
    var createdAt: String? = null,

    @SerializedName("gps")
    var gps: String? = null,

    @SerializedName("deleted_at")
    var deletedAt: String? = null,

    @SerializedName("nomor")
    var nomor: String? = null,

    @SerializedName("aktif")
    var aktif: String? = null,

    @SerializedName("kode")
    var kode: String? = null,

    @SerializedName("lock")
    var lock: String? = null,

    @SerializedName("petani")
    var petani: String? = null,

    @SerializedName("ID")
    var iD: Int? = null,

    @SerializedName("modified_at")
    var modifiedAt: String? = null,

    @SerializedName("no_dokumen")
    var noDokumen: String? = null,

    @SerializedName("deskripsi")
    var deskripsi: String? = null,

    @SerializedName("foto_lahan")
    var fotoLahan: String? = null,

    @SerializedName("uri_foto")
    var uriFoto: String? = null,

    @SerializedName("status")
    var status: String? = null
)