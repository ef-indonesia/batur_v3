package com.traceon.batur.data.response

import com.traceon.batur.data.model.Visit
import com.traceon.batur.data.model.VisitDetail

data class ResponseIncidentalVisit(
    val data: VisitDetail,
    val message: String,
    val result: Boolean,
    val status: String
)