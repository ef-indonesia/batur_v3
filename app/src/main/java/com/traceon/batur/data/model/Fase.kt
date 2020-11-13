package com.traceon.batur.data.model

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Fase(
    @PrimaryKey
    var ID: String? = null,
    var aktif: String? = null,
    var bobot: String? = null,
    var created_at: String? = null,
    var deleted_at: String? = null,
    var group_komoditas_ID: String? = null,
    var modified_at: String? = null,
    var nama: String? = null,
    var nomor: String? = null
) : RealmObject(), Parcelable