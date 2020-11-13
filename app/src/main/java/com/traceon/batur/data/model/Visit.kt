package com.traceon.batur.data.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Visit(

    @SerializedName("baseline_data_ID")
    var baseline_data_ID: String? = null,

    @SerializedName("desa")
    var desa: String? = null,

    @SerializedName("desa_ID")
    var desa_ID: String? = null,

    @SerializedName("group_komoditas_ID")
    var group_komoditas_ID: String? = null,

    @SerializedName("kode_lahan")
    var kode_lahan: String? = null,

    @SerializedName("komoditas")
    var komoditas: String? = null,

    @SerializedName("lahan_ID")
    var lahan_ID: String? = null,

    @SerializedName("petani")
    var petani: String? = null,

    @SerializedName("petani_ID")
    var petani_ID: String? = null,

    @SerializedName("program_visit_ID")
    var program_visit_ID: String? = null,

    var no_urut: Int = 0,

    var warna: String? = null,
    @PrimaryKey
    @SerializedName("ID")

    var id: Long? = 0

) : RealmObject()