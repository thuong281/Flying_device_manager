package com.example.flyingdevicemanager.models


import com.google.gson.annotations.SerializedName

data class Device2(
    @SerializedName("buyDate")
    val buyDate: String? = "",
    @SerializedName("coordinates")
    val coordinates: List<Any?>? = listOf(),
    @SerializedName("createdDate")
    val createdDate: Long? = 0,
    @SerializedName("createdUser")
    val createdUser: String? = "",
    @SerializedName("deviceColor")
    val deviceColor: String? = "",
    @SerializedName("deviceManufacturer")
    val deviceManufacturer: String? = "",
    @SerializedName("devicePlate")
    val devicePlate: String? = "",
    @SerializedName("_id")
    val id: String? = "",
    @SerializedName("lastUpdated")
    val lastUpdated: Long? = 0,
    @SerializedName("registerNationalId")
    val registerNationalId: String? = "",
    @SerializedName("updatedUser")
    val updatedUser: String? = "",
    @SerializedName("__v")
    val v: Int? = 0,
    @SerializedName("isActive")
    val isActive: Boolean? = false,
    @SerializedName("updatedLocationTime")
    val updatedLocationTime: Long? = 0
)