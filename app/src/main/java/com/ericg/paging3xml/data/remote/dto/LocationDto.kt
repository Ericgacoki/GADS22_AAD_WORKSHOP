package com.ericg.paging3xml.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LocationDto(
    @SerializedName("name")
    val locationName: String,
    @SerializedName("url")
    val locationUrl: String
)