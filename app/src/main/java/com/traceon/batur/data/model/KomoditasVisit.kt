package com.traceon.batur.data.model

import android.os.Parcelable
import io.realm.RealmObject
import kotlinx.android.parcel.Parcelize

@Parcelize
open class KomoditasVisit(
    var ID: String? = null,
    var aktif: String? = null,
    var author: String? = null,
    var created_at: String? = null,
    var deleted_at: String? = null,
    var group_komoditas_ID: String? = null,
    var kode: String? = null,
    var lock: String? = null,
    var luas_pemanfaatan: String? = null,
    var mekanisme: String? = null,
    var modified_at: String? = null,
    var nama: String? = null,
    var nilai: String? = null,
    var pemanfaatan: String? = null,
    var produktifitas: String? = null,
    var ras: String? = null,
    var sub_lahan_ID: String? = null
) : RealmObject(), Parcelable