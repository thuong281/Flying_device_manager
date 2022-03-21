package com.example.flyingdevicemanager.models


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("lat")
    val lat: Double? = 0.0,
    @SerializedName("long")
    val long: Double? = 0.0
)