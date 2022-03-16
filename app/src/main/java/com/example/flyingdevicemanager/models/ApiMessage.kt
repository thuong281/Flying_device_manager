package com.example.flyingdevicemanager.models

import com.google.gson.annotations.SerializedName

data class ApiMessage(
    @SerializedName("msg")
    val errorMessage: String = "Unknown error"
)