package com.ericg.paging3xml.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OriginDto(
    @SerializedName("name")
    val originName: String,
    @SerializedName("url")
    val originUrl: String
)