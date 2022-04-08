package com.example.flyingdevicemanager.models

import com.google.gson.annotations.SerializedName

data class CountSummary(
    @SerializedName("devices")
    val devices: Int? = 0,
    @SerializedName("active_devices")
    val activeDevices: Int? = 0,
    @SerializedName("registers")
    val registers: Int? = 0,
)
