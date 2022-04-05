package com.example.flyingdevicemanager.util.base

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("msg")
    val msg: String,
    @SerializedName("data")
    val data: T
)
