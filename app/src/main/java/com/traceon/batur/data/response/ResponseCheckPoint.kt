package com.traceon.batur.data.response

import com.traceon.batur.data.model.CheckPoint

data class ResponseCheckPoint(
    val `data`: List<CheckPoint>,
    val message: String,
    val result: Boolean,
    val status: String
)