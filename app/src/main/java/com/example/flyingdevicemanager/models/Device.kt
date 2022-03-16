package com.example.flyingdevicemanager.models


import com.google.gson.annotations.SerializedName

data class Device(
    @SerializedName("device_id")
    val deviceId: String? = "",
    @SerializedName("device_name")
    val deviceName: String? = "",
    @SerializedName("isOnline")
    val isOnline: Int? = 0
)