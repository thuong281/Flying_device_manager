package com.example.flyingdevicemanager.models

import com.google.gson.annotations.SerializedName

data class Register(
    @SerializedName("_id")
    val id: String? = "",
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("nationalId")
    val registerId: String? = "",
    @SerializedName("phoneNumber")
    val phoneNumber: String? = "",
    @SerializedName("listDeviceId")
    val listDeviceId: List<String>? = ArrayList(),
    @SerializedName("isOrganization")
    val isOrganization: Int?
)
