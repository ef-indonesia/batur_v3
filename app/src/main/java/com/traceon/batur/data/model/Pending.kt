package com.traceon.batur.data.model

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Pending(
    @PrimaryKey
    var id: Int? = null,
    var idPost: Int? = null,
    var type: Int? = null,
    var desc: String? = null,
    var status: Int? = null,
    var created_at: String? = null,
    var updated_ad: String? = null
) : RealmObject(), Parcelable