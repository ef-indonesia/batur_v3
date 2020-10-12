package com.traceon.batur.data.response

import com.google.gson.annotations.SerializedName
import com.traceon.batur.data.model.Visit

data class ResponsePrioritasVisit(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("result")
    val result: Boolean? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("data")
    val data: List<Visit>? = null
)