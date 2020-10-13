package com.traceon.batur.data.response

import com.google.gson.annotations.SerializedName
import com.traceon.batur.data.model.VisitDetail

data class ResponseIncidentalVisit(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("result")
    val result: Boolean? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("data")
    val data: List<VisitDetail>? = null
)