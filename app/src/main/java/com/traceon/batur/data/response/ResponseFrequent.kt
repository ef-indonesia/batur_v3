package com.traceon.batur.data.response

import com.traceon.batur.data.model.Frequent

data class ResponseFrequent(
    val data: Frequent,
    val message: String,
    val result: Boolean,
    val status: String
)