package com.traceon.batur.data.response

import com.google.gson.annotations.SerializedName

data class ResponseProfile(
    @SerializedName("email")
    var email: String? = null,

    @SerializedName("username")
    var username: String? = null
)