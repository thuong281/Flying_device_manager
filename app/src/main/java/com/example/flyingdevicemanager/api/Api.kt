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
        @Header("auth-token") token: String,
        @Query("word") word: String
    ): Response<BaseResponse<List<UserFollow>>>
    
    // send follow request
    @PUT("api/user/request-follow")
    @FormUrlEncoded
    suspend fun requestFollow(
        @Header("auth-token") token: String,
        @Field("user_id") userId: String
    ): Response<BaseResponse<Any>>
    
    // get list user request to follow
    @GET("api/user/pending-follow-request")
    suspend fun getListRequestFollow(
        @Header("auth-token") token: String
    ): Response<BaseResponse<List<User>>>
    
    // get list follow of user
    @GET("api/user/follow")
    suspend fun getListFollow(
        @Header("auth-token") token: String
    ): Response<BaseResponse<List<User>>>
    
    // approve/reject follow request
    @PUT("api/user/confirm-follow")
    @FormUrlEncoded
    suspend fun responseRequestFollow(
        @Header("auth-token") token: String,
        @Field("approve") approve: Int,
        @Field("user_request") userRequestId: String
    ): Response<BaseResponse<Any>>
    
    // get device location
    @GET("api/device/get-location")
    suspend fun getDeviceLocation(
        @Header("auth-token") token: String,
        @Query("device_id") deviceId: String
    ): Response<BaseResponse<Location>>
    
    // get current user
    @GET("api/user")
    suspend fun getCurrentUser(
        @Header("auth-token") token: String,
    ): Response<BaseResponse<User>>
    
    // change user avatar
    @PUT("api/user/avatar")
    @Multipart
    suspend fun updateAvatar(
        @Header("auth-token") token: String,
        @Part image: MultipartBody.Part
    ): Response<BaseResponse<Any>>
    
}