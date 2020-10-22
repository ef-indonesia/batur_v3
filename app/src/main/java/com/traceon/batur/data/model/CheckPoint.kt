package com.traceon.batur.data.model

data class CheckPoint(
    val ID: String,
    val bobot: String,
    val check_list: List<String>,
    val check_point: String,
    val jumlah_check_list: Int,
    val nilai: List<String>,
    val rank: String,
    val standard: String
)