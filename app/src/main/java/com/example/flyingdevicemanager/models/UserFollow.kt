package com.example.flyingdevicemanager.models


import com.google.gson.annotations.SerializedName

data class UserFollow(
    @SerializedName("email")
    val email: String? = "",
    @SerializedName("status")
    val status: Int? = 0,
    @SerializedName("avatar")
    val avatar: String? = "",
    @SerializedName("user_id")
    val userId: String? = "",
    @SerializedName("user_name")
    val userName: String? = ""
)