package com.example.flyingdevicemanager.models


import com.google.gson.annotations.SerializedName

data class UserToken(
    @SerializedName("token")
    val token: String? = ""
)