package com.example.flyingdevicemanager.models

import com.google.gson.annotations.SerializedName

data class Register(
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("national_id")
    val nationalId: String? = "",
    @SerializedName("phone_number")
    val phoneNumber: String? = "",
    @SerializedName("device_count")
    val deviceCount: Int? = 0,
)
