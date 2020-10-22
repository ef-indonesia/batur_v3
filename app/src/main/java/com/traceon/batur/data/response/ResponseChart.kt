package com.traceon.batur.data.response

data class ResponseChart(
    val data: DataResult,
    val message: String,
    val result: Boolean,
    val status: String
)

data class DataResult(
    val jumlah_komoditas: Int,
    val list_detail_komoditas: List<DataChart>,
    val list_komoditas: List<String>
)

data class DataChart(
    val group_komoditas_ID: String,
    val jumlah_petani_fase_panen: Float,
    val jumlah_petani_fase_pembibitan: Float,
    val jumlah_petani_fase_pemeliharaan: Float,
    val jumlah_petani_fase_persiapan: Float,
    val jumlah_petani_total: Float,
    val komoditas: String,
    val luas_lahan_fase_panen: Float,
    val luas_lahan_fase_pembibitan: Float,
    val luas_lahan_fase_pemeliharaan: Float,
    val luas_lahan_fase_persiapan: Float,
    val luas_lahan_total: Float
)