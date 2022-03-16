package com.example.flyingdevicemanager.models


import com.google.gson.annotations.SerializedName

data class UserKyc(
    @SerializedName("backImage")
    val backImage: String? = "",
    @SerializedName("cloudinary_id_1")
    val cloudinaryId1: String? = "",
    @SerializedName("cloudinary_id_2")
    val cloudinaryId2: String? = "",
    @SerializedName("frontImage")
    val frontImage: String? = "",
    @SerializedName("_id")
    val id: String? = "",
    @SerializedName("nationalId")
    val nationalId: String? = "",
    @SerializedName("userId")
    val userId: String? = "",
    @SerializedName("__v")
    val v: Int? = 0,
    @SerializedName("verified")
    val verified: Int? = 0
)