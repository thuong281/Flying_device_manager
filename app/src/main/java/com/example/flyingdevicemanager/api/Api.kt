package com.example.flyingdevicemanager.api

import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.util.BaseResponse
import okhttp3.*
import retrofit2.Response
import retrofit2.http.*

interface Api {
    
    // login
    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<BaseResponse<UserToken>>
    
    // signup
    @POST("register")
    @FormUrlEncoded
    suspend fun signup(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("password") password: String
    ): Response<User>
    
    // request kyc
    @POST("api/user/upload-kyc")
    @Multipart
    suspend fun requestKyc(
        @Header("auth-token") token: String,
        @Part image: ArrayList<MultipartBody.Part>,
        @Part("national_id") nationalId: RequestBody
    ): Response<ApiMessage>
    
    // get currentUserKYC
    @GET("api/user/user-kyc")
    suspend fun getUserKyc(
        @Header("auth-token") token: String,
    ): Response<BaseResponse<UserKyc>>
    
    // get current user device list
    @GET("api/device/user-device-list")
    suspend fun getUserDevices(
        @Header("auth-token") token: String,
    ): Response<BaseResponse<List<Device>>>
    
    // add device
    @PUT("api/device/insert-device")
    @FormUrlEncoded
    suspend fun addDevice(
        @Header("auth-token") token: String,
        @Field("device_name") deviceName: String
    ): Response<BaseResponse<Any>>
    
    // delete device
    @DELETE("api/device/delete-device/{device_id}")
    suspend fun deleteDevice(
        @Header("auth-token") token: String,
        @Path("device_id") deviceId: String
    ): Response<BaseResponse<Any>>
    
    // search user
    @GET("api/user/find-user")
    suspend fun searchUser(
        @Query("word") word: String
    ): Response<BaseResponse<UserFollow>>
    
}