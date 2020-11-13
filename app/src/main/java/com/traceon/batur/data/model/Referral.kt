package com.traceon.batur.data.model

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Referral(
    @PrimaryKey
    var iD: Int? = null,
    var desaID: Int? = null,
    var desa: String? = null,
    var kodeReferal: String? = null,
    var penetapanDesaID: Int? = null,
    var createdAt: String? = null,
    var areaID: Int? = null,
    var deletedAt: String? = null,
    var aktif: String? = null,
    var masaAktif: String? = null,
    var manajemenUnitID: Int? = null,
    var staffID: Int? = null,
    var modifiedAt: String? = null,
    var sisaHari: Int? = null
) : RealmObject(), Parcelable