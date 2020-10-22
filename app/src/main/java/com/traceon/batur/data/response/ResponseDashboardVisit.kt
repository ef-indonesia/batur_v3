package com.traceon.batur.data.response

data class ResponseDashboardVisit(
    val data: DataDashboard,
    val message: String,
    val result: Boolean,
    val status: String
)

data class DataDashboard(
    val jumlah_komoditas: Int,
    val list_detail_komoditas: List<DashboardChart>,
    val list_komoditas: List<String>
)

data class DashboardChart(
    val detail_fase_komoditas: List<DetailFaseKomodita>,
    val group_komoditas_ID: String,
    val jumlah_fase_komoditas: Int,
    val jumlah_petani_total: Int,
    val komoditas: String,
    val luas_lahan_total: Float
)

data class DetailFaseKomodita(
    val color_hexa: String,
    val color_materialize: String,
    val jumlah_petani_fase: Int,
    val luas_lahan_fase: Float,
    val nama: String
)


