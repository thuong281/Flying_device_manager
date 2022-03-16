package com.example.flyingdevicemanager.models


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("date")
    val date: String? = "",
    @SerializedName("email")
    val email: String? = "",
    @SerializedName("_id")
    val id: String? = "",
    @SerializedName("isAdmin")
    val isAdmin: String? = "",
    @SerializedName("listDevice")
    val listDevice: List<Any?>? = listOf(),
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("password")
    val password: String? = "",
    @SerializedName("userFollower")
    val userFollower: List<Any?>? = listOf(),
    @SerializedName("userFollowerPending")
    val userFollowerPending: List<Any?>? = listOf(),
    @SerializedName("userFollowing")
    val userFollowing: List<Any?>? = listOf(),
    @SerializedName("userFollowingPending")
    val userFollowingPending: List<Any?>? = listOf(),
    @SerializedName("__v")
    val v: Int? = 0
)