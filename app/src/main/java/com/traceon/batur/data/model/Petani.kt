package com.traceon.batur.data.model

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Petani(
    @PrimaryKey
    var ID: Int? = null,
    var agama: String? = null,
    var aktif: String? = null,
    var alamat: String? = null,
    var area: String? = null,
    var area_ID: Int? = null,
    var asal_usul_petani: String? = null,
    var atas_nama: String? = null,
    var bank_ID: String? = null,
    var created_at: String? = null,
    var deleted_at: String? = null,
    var desa_ID: Int? = null,
    var email: String? = null,
    var foto_ktp: String? = null,
    var foto_petani: String? = null,
    var foto_surat_kuasa: String? = null,
    var foto_tanda_tangan: String? = null,
    var jenis_kelamin: String? = null,
    var jumlah_anggota_keluarga: String? = null,
    var jumlah_pendapatan_tahunan: String? = null,
    var kelompok_tani: String? = null,
    var kode: String? = null,
    var kode_desa: String? = null,
    var kode_pos: String? = null,
    var kode_rahasia: String? = null,
    var lahan_kunci: Int? = null,
    var lahan_total: Int? = null,
    var manajemen_unit: String? = null,
    var manajemen_unit_ID: Int? = null,
    var modified_at: String? = null,
    var nama: String? = null,
    var no_ktp: String? = null,
    var no_rekening: String? = null,
    var no_telp: String? = null,
    var nomor: String? = null,
    var pekerjaan_sampingan: String? = null,
    var pekerjaan_utama: String? = null,
    var pendidikan_non_formal: String? = null,
    var pendidikan_terakhir: String? = null,
    var posisi_dalam_kelompok_tani: String? = null,
    var status: String? = null,
    var suku: String? = null,
    var tanggal_lahir: String? = null,
    var tempat_lahir: String? = null,
    var ukuran_text: Double? = null,
    var ukuran_total: Double? = null
) : RealmObject(), Parcelable