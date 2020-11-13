package com.traceon.batur.data.model

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Baseline(
    @PrimaryKey
    var ID: Int? = null,
    var aktif: String? = null,
    var author: String? = null,
    var created_at: String? = null,
    var deleted_at: String? = null,
    var foto_baseline: String? = null,
    var gps: String? = null,
    var group_komoditas_ID: Int? = null,
    var kode_lahan: String? = null,
    var komoditas: String? = null,
    var lahan_ID: Int? = null,
    var lock: String? = null,
    var luas_lahan: String? = null,
    var luas_pemanfaatan: String? = null,
    var mekanisme: String? = null,
    var modified_at: String? = null,
    var nilai: String? = null,
    var pemanfaatan: String? = null,
    var petani_ID: Int? = null,
    var produktifitas: String? = null,
    var ras: String? = null,
    var sub_lahan_ID: Int? = null
) : RealmObject(), Parcelable