package com.example.flyingdevicemanager.util

import com.example.flyingdevicemanager.api.RetrofitInstance
import com.example.flyingdevicemanager.models.ApiMessage
import okhttp3.ResponseBody
import retrofit2.*
import java.io.IOException


class ErrorUtils {
    companion object {
        fun parseMessage(response: Response<Any>): ApiMessage {
            val converter: Converter<ResponseBody, ApiMessage> =
                RetrofitInstance.retrofit.responseBodyConverter(
                    ApiMessage::class.java, arrayOfNulls<Annotation>(0)
                )
            var apiMessage: ApiMessage = ApiMessage()
            try {
                apiMessage = converter.convert(response.errorBody())!!
            } catch (e: IOException) {
                return apiMessage
            }
            return apiMessage
        }
        
    }
}