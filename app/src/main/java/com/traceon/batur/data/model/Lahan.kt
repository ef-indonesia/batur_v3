package com.traceon.batur.data.model

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Lahan(
    @PrimaryKey
    var ID: Int? = null,
    var akses_jalan: String? = null,
    var akses_ke_sumber_air: String? = null,
    var aktif: String? = null,
    var blok: String? = null,
    var created_at: String? = null,
    var deleted_at: String? = null,
    var desa_ID: Int? = null,
    var deskripsi: String? = null,
    var foto_dokumen: String? = null,
    var foto_lahan: String? = null,
    var gps: String? = null,
    var informasi_jenis_tanaman: String? = null,
    var jenis_tanah: String? = null,
    var kemiringan_lahan: String? = null,
    var ketinggian_tempat: String? = null,
    var kode: String? = null,
    var lock: String? = null,
    var luas_lahan: String? = null,
    var modified_at: String? = null,
    var no_dokumen: String? = null,
    var nomor: String? = null,
    var pemupukan: String? = null,
    var pestisida: String? = null,
    var petani: String? = null,
    var petani_ID: Int? = null,
    var potensi_pemanfaatan_lahan: String? = null,
    var sejarah_budidaya_lahan: String? = null,
    var status_kepemelikikan_lahan: String? = null,
    var sub_blok: String? = null,
    var tutupan_lahan: String? = null
) : RealmObject(), Parcelable