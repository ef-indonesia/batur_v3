package com.traceon.batur.data.model

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Satuan(
    @PrimaryKey
    var ID: String? = null,
    var aktif: String? = null,
    var created_at: String? = null,
    var deleted_at: String? = null,
    var grup: String? = null,
    var keterangan: String? = null,
    var kode: String? = null,
    var modified_at: String? = null,
    var pengali: String? = null
) : RealmObject(), Parcelable