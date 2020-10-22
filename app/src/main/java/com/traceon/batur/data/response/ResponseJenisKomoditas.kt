package com.traceon.batur.data.response

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.traceon.batur.data.model.Satuan
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseJenisKomoditas(
    @SerializedName("nama_slovak")
    var namaSlovak: String? = null,

    @SerializedName("nama_latvian")
    var namaLatvian: String? = null,

    @SerializedName("nama_english")
    var namaEnglish: String? = null,

    @SerializedName("nama_filipino")
    var namaFilipino: String? = null,

    @SerializedName("nama_swedish")
    var namaSwedish: String? = null,

    @SerializedName("nama_portuguesebrazilian")
    var namaPortuguesebrazilian: String? = null,

    @SerializedName("nama_czech")
    var namaCzech: String? = null,

    @SerializedName("nama_arabic")
    var namaArabic: String? = null,

    @SerializedName("modified_at")
    var modifiedAt: String? = null,

    @SerializedName("nama_dutch")
    var namaDutch: String? = null,

    @SerializedName("nama_urdu")
    var namaUrdu: String? = null,

    @SerializedName("nama_basque")
    var namaBasque: String? = null,

    @SerializedName("nama_polish")
    var namaPolish: String? = null,

    @SerializedName("nama_serbian")
    var namaSerbian: String? = null,

    @SerializedName("nama_persian")
    var namaPersian: String? = null,

    @SerializedName("nama_danish")
    var namaDanish: String? = null,

    @SerializedName("nama_traditionalchinese")
    var namaTraditionalchinese: String? = null,

    @SerializedName("nama_romanian")
    var namaRomanian: String? = null,

    @SerializedName("nama_russian")
    var namaRussian: String? = null,

    @SerializedName("nama_simplifiedchinese")
    var namaSimplifiedchinese: String? = null,

    @SerializedName("deleted_at")
    var deletedAt: String? = null,

    @SerializedName("aktif")
    var aktif: String? = null,

    @SerializedName("sub_kategori_komoditas_ID")
    var subKategoriKomoditasID: String? = null,

    @SerializedName("nama")
    var nama: String? = null,

    @SerializedName("nama_gujarati")
    var namaGujarati: String? = null,

    @SerializedName("kategori_komoditas")
    var kategoriKomoditas: String? = null,

    @SerializedName("nama_armenian")
    var namaArmenian: String? = null,

    @SerializedName("nama_hindi")
    var namaHindi: String? = null,

    @SerializedName("nama_german")
    var namaGerman: String? = null,

    @SerializedName("nama_japanese")
    var namaJapanese: String? = null,

    @SerializedName("nama_turkish")
    var namaTurkish: String? = null,

    @SerializedName("sub_kategori_komoditas")
    var subKategoriKomoditas: String? = null,

    @SerializedName("created_at")
    var createdAt: String? = null,

    @SerializedName("nama_lithuanian")
    var namaLithuanian: String? = null,

    @SerializedName("nama_tamil")
    var namaTamil: String? = null,

    @SerializedName("nama_bengali")
    var namaBengali: String? = null,

    @SerializedName("nama_french")
    var namaFrench: String? = null,

    @SerializedName("nama_portuguese")
    var namaPortuguese: String? = null,

    @SerializedName("kode")
    var kode: String? = null,

    @SerializedName("nama_korean")
    var namaKorean: String? = null,

    @SerializedName("nama_croatian")
    var namaCroatian: String? = null,

    @SerializedName("nama_greek")
    var namaGreek: String? = null,

    @SerializedName("nama_ukrainian")
    var namaUkrainian: String? = null,

    @SerializedName("nama_italian")
    var namaItalian: String? = null,

    @SerializedName("nama_catalan")
    var namaCatalan: String? = null,

    @SerializedName("nama_norwegian")
    var namaNorwegian: String? = null,

    @SerializedName("nama_spanish")
    var namaSpanish: String? = null,

    @SerializedName("satuan_ID")
    var satuanID: String? = null,

    @SerializedName("nama_vietnamese")
    var namaVietnamese: String? = null,

    @SerializedName("nama_thai")
    var namaThai: String? = null,

    @SerializedName("nama_khmer")
    var namaKhmer: String? = null,

    @SerializedName("nama_indonesian")
    var namaIndonesian: String? = null,

    @SerializedName("nama_bulgarian")
    var namaBulgarian: String? = null,

    @SerializedName("nama_hungarian")
    var namaHungarian: String? = null,

    @SerializedName("keterangan_satuan")
    var keteranganSatuan: String? = null,

    @SerializedName("kode_satuan")
    var kodeSatuan: String? = null,

    @SerializedName("satuan")
    var satuan: List<Satuan>? = null,

    @SerializedName("nama_slovenian")
    var namaSlovenian: String? = null,

    @SerializedName("nama_azerbaijani")
    var namaAzerbaijani: String? = null,

    @PrimaryKey
    @SerializedName("ID")
    var iD: String? = null

) : Parcelable