package com.example.flyingdevicemanager.util

import com.google.gson.annotations.SerializedName
import retrofit2.Response

data class BaseResponse<T>(
    @SerializedName("msg")
    val msg: String,
    @SerializedName("data")
    val data: T
)
