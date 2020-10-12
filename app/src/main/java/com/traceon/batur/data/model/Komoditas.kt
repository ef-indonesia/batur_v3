package com.traceon.batur.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "komoditases")
data class Komoditas(
    @SerializedName("nama_slovak")
    @ColumnInfo(name = "nama_slovak")
    var namaSlovak: String? = null,

    @SerializedName("nama_latvian")
    @ColumnInfo(name = "nama_latvian")
    var namaLatvian: String? = null,

    @SerializedName("nama_english")
    @ColumnInfo(name = "nama_english")
    var namaEnglish: String? = null,

    @SerializedName("nama_filipino")
    @ColumnInfo(name = "nama_filipino")
    var namaFilipino: String? = null,

    @SerializedName("nama_swedish")
    @ColumnInfo(name = "nama_swedish")
    var namaSwedish: String? = null,

    @SerializedName("nama_portuguesebrazilian")
    @ColumnInfo(name = "nama_portuguesebrazilian")
    var namaPortuguesebrazilian: String? = null,

    @SerializedName("nama_czech")
    @ColumnInfo(name = "nama_czech")
    var namaCzech: String? = null,

    @SerializedName("nama_arabic")
    @ColumnInfo(name = "nama_arabic")
    var namaArabic: String? = null,

    @SerializedName("modified_at")
    @ColumnInfo(name = "modified_at")
    var modifiedAt: String? = null,

    @SerializedName("nama_dutch")
    @ColumnInfo(name = "nama_dutch")
    var namaDutch: String? = null,

    @SerializedName("nama_urdu")
    @ColumnInfo(name = "nama_urdu")
    var namaUrdu: String? = null,

    @SerializedName("nama_basque")
    @ColumnInfo(name = "nama_basque")
    var namaBasque: String? = null,

    @SerializedName("nama_polish")
    @ColumnInfo(name = "nama_polish")
    var namaPolish: String? = null,

    @SerializedName("nama_serbian")
    @ColumnInfo(name = "nama_serbian")
    var namaSerbian: String? = null,

    @SerializedName("nama_persian")
    @ColumnInfo(name = "nama_persian")
    var namaPersian: String? = null,

    @SerializedName("nama_danish")
    @ColumnInfo(name = "nama_danish")
    var namaDanish: String? = null,

    @SerializedName("nama_traditionalchinese")
    @ColumnInfo(name = "nama_traditionalchinese")
    var namaTraditionalchinese: String? = null,

    @SerializedName("nama_romanian")
    @ColumnInfo(name = "nama_romanian")
    var namaRomanian: String? = null,

    @SerializedName("nama_russian")
    @ColumnInfo(name = "nama_russian")
    var namaRussian: String? = null,

    @SerializedName("nama_simplifiedchinese")
    @ColumnInfo(name = "nama_simplifiedchinese")
    var namaSimplifiedchinese: String? = null,

    @SerializedName("deleted_at")
    @ColumnInfo(name = "deleted_at")
    var deletedAt: String? = null,

    @SerializedName("aktif")
    @ColumnInfo(name = "aktif")
    var aktif: String? = null,

    @SerializedName("sub_kategori_komoditas_ID")
    @ColumnInfo(name = "sub_kategori_komoditas_ID")
    var subKategoriKomoditasID: String? = null,

    @SerializedName("nama")
    @ColumnInfo(name = "nama")
    var nama: String? = null,

    @SerializedName("nama_gujarati")
    @ColumnInfo(name = "nama_gujarati")
    var namaGujarati: String? = null,

    @SerializedName("kategori_komoditas")
    @ColumnInfo(name = "kategori_komoditas")
    var kategoriKomoditas: String? = null,

    @SerializedName("nama_armenian")
    @ColumnInfo(name = "nama_armenian")
    var namaArmenian: String? = null,

    @SerializedName("nama_hindi")
    @ColumnInfo(name = "nama_hindi")
    var namaHindi: String? = null,

    @SerializedName("nama_german")
    @ColumnInfo(name = "nama_german")
    var namaGerman: String? = null,

    @SerializedName("nama_japanese")
    @ColumnInfo(name = "nama_japanese")
    var namaJapanese: String? = null,

    @SerializedName("nama_turkish")
    @ColumnInfo(name = "nama_turkish")
    var namaTurkish: String? = null,

    @SerializedName("sub_kategori_komoditas")
    @ColumnInfo(name = "sub_kategori_komoditas")
    var subKategoriKomoditas: String? = null,

    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null,

    @SerializedName("nama_lithuanian")
    @ColumnInfo(name = "nama_lithuanian")
    var namaLithuanian: String? = null,

    @SerializedName("nama_tamil")
    @ColumnInfo(name = "nama_tamil")
    var namaTamil: String? = null,

    @SerializedName("nama_bengali")
    @ColumnInfo(name = "nama_bengali")
    var namaBengali: String? = null,

    @SerializedName("nama_french")
    @ColumnInfo(name = "nama_french")
    var namaFrench: String? = null,

    @SerializedName("nama_portuguese")
    @ColumnInfo(name = "nama_portuguese")
    var namaPortuguese: String? = null,

    @SerializedName("kode")
    @ColumnInfo(name = "kode")
    var kode: String? = null,

    @SerializedName("nama_korean")
    @ColumnInfo(name = "nama_korean")
    var namaKorean: String? = null,

    @SerializedName("nama_croatian")
    @ColumnInfo(name = "nama_croatian")
    var namaCroatian: String? = null,

    @SerializedName("nama_greek")
    @ColumnInfo(name = "nama_greek")
    var namaGreek: String? = null,

    @SerializedName("nama_ukrainian")
    @ColumnInfo(name = "nama_ukrainian")
    var namaUkrainian: String? = null,

    @SerializedName("nama_italian")
    @ColumnInfo(name = "nama_italian")
    var namaItalian: String? = null,

    @SerializedName("nama_catalan")
    @ColumnInfo(name = "nama_catalan")
    var namaCatalan: String? = null,

    @SerializedName("nama_norwegian")
    @ColumnInfo(name = "nama_norwegian")
    var namaNorwegian: String? = null,

    @SerializedName("nama_spanish")
    @ColumnInfo(name = "nama_spanish")
    var namaSpanish: String? = null,

    @SerializedName("satuan_ID")
    @ColumnInfo(name = "satuan_ID")
    var satuanID: String? = null,

    @SerializedName("nama_vietnamese")
    @ColumnInfo(name = "nama_vietnamese")
    var namaVietnamese: String? = null,

    @SerializedName("nama_thai")
    @ColumnInfo(name = "nama_thai")
    var namaThai: String? = null,

    @SerializedName("nama_khmer")
    @ColumnInfo(name = "nama_khmer")
    var namaKhmer: String? = null,

    @SerializedName("nama_indonesian")
    var namaIndonesian: String? = null,

    @SerializedName("nama_bulgarian")
    @ColumnInfo(name = "nama_bulgarian")
    var namaBulgarian: String? = null,

    @SerializedName("nama_hungarian")
    @ColumnInfo(name = "nama_hungarian")
    var namaHungarian: String? = null,

    @SerializedName("satuan")
    @ColumnInfo(name = "satuan")
    var satuan: String? = null,

    @SerializedName("keterangan_satuan")
    @ColumnInfo(name = "keterangan_satuan")
    var keteranganSatuan: String? = null,

    @SerializedName("kode_satuan")
    @ColumnInfo(name = "kode_satuan")
    var kodeSatuan: String? = null,

    @SerializedName("nama_slovenian")
    @ColumnInfo(name = "nama_slovenian")
    var namaSlovenian: String? = null,

    @SerializedName("nama_azerbaijani")
    @ColumnInfo(name = "nama_azerbaijani")
    var namaAzerbaijani: String? = null,

    @PrimaryKey
    @SerializedName("ID")
    @ColumnInfo(name = "ID")
    var iD: Int? = null

) : Parcelable