package com.example.flyingdevicemanager.api

import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.util.base.BaseResponse
import okhttp3.*
import retrofit2.Response
import retrofit2.http.*

interface Api {
    
    // old part
    
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
    suspend fun getCurrentUserDevices(
        @Header("auth-token") token: String,
    ): Response<BaseResponse<List<Device>>>
    
    // get other user device list
    @GET("api/device/device-list")
    suspend fun getUserDevices(
        @Header("auth-token") token: String,
        @Query("user_id") userId: String
    ): Response<BaseResponse<List<Device>>>
    
    // add device
    @PUT("api/device/insert-device")
    @FormUrlEncoded
    suspend fun addDevice(
        @Header("auth-token") token: String,
        @Field("device_name") deviceName: String
    ): Response<BaseResponse<Any>>
    
    // delete device
//    @DELETE("api/device/delete-device/{device_id}")
//    suspend fun deleteDevice(
//        @Header("auth-token") token: String,
//        @Path("device_id") deviceId: String
//    ): Response<BaseResponse<Any>>
    
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
    @POST("api/user/avatar")
    @Multipart
    suspend fun updateAvatar(
        @Header("auth-token") token: String,
        @Part image: MultipartBody.Part
    ): Response<BaseResponse<Any>>
    
    // get all user that kyc is not verified
    @GET("api/user/users-kyc/{page}")
    suspend fun getAllUserKyc(
        @Header("auth-token") token: String,
        @Path("page") page: Int
    ): Response<BaseResponse<List<User>>>
    
    // get other user kyc
    @GET("api/user/other-user-kyc")
    suspend fun getOtherUserKyc(
        @Header("auth-token") token: String,
        @Query("user_id") userId: String
    ): Response<BaseResponse<UserKyc>>
    
    // approve user kyc
    @PUT("api/user/user-kyc/confirm/{user_id}")
    suspend fun approveUserKyc(
        @Header("auth-token") token: String,
        @Path("user_id") userId: String
    ): Response<BaseResponse<Any>>
    
    // reject user kyc
    @DELETE("api/user/user-kyc/delete/{user_id}")
    suspend fun rejectUserKyc(
        @Header("auth-token") token: String,
        @Path("user_id") userId: String
    ): Response<BaseResponse<Any>>
    
    
    // new part
    @POST("api/device/insert")
    @FormUrlEncoded
    suspend fun insertNewDevice(
        @Header("auth-token") token: String,
        @Field("register_name") registerName: String,
        @Field("register_national_id") registerNationalId: String,
        @Field("register_phone") registerPhone: String,
        @Field("device_plate") devicePlate: String,
        @Field("device_color") deviceColor: String,
        @Field("device_manufacturer") deviceManufacturer: String,
        @Field("device_buy_date") deviceBuyDate: String,
    ): Response<BaseResponse<Any>>
    
    // get list add device history
    @GET("api/device/insert-history/{page}")
    suspend fun getListAddDeviceHistory(
        @Header("auth-token") token: String,
        @Path("page") page: Int
    ): Response<BaseResponse<List<Device2>>>
    
    // search register
    @GET("api/register/search")
    suspend fun searchRegister(
        @Header("auth-token") token: String,
        @Query("word") word: String
    ): Response<BaseResponse<List<Register>>>
    
    // search device
    @GET("api/device/search")
    suspend fun searchDevice(
        @Header("auth-token") token: String,
        @Query("word") word: String
    ): Response<BaseResponse<List<Device2>>>
    
    // get device by device id
    @GET("api/device")
    suspend fun getDeviceById(
        @Header("auth-token") token: String,
        @Query("id") id: String
    ): Response<BaseResponse<Device2>>
    
    // update device
    @PUT("api/device/update")
    @FormUrlEncoded
    suspend fun updateDevice(
        @Header("auth-token") token: String,
        @Field("device_id") deviceId: String,
        @Field("device_palate") devicePlate: String,
        @Field("device_color") deviceColor: String,
        @Field("device_manufacturer") deviceManufacturer: String,
        @Field("device_buy_date") deviceBuyDate: String,
    ): Response<BaseResponse<Any>>
    
    // delete device
    @DELETE("api/device/{device_id}")
    suspend fun deleteDevice(
        @Header("auth-token") token: String,
        @Path("device_id") deviceId: String
    ): Response<BaseResponse<Any>>
    
    // get single register by id
    @GET("/api/register/")
    suspend fun getRegister(
        @Header("auth-token") token: String,
        @Query("id") id: String
    ): Response<BaseResponse<Register>>
    
    // get list device of register
    @GET("/api/register/devices")
    suspend fun getListDevices(
        @Header("auth-token") token: String,
        @Query("id") id: String
    ): Response<BaseResponse<List<Device2>>>
    
    // update register
    @PUT("api/register/update")
    @FormUrlEncoded
    suspend fun updateRegister(
        @Header("auth-token") token: String,
        @Field("register_id") registerId: String,
        @Field("name") name: String,
        @Field("national_id") nationalId: String,
        @Field("phone_number") phoneNumber: String,
    ): Response<BaseResponse<Any>>
    
    // delete register
    @DELETE("/api/register/{id}")
    suspend fun deleteRegister(
        @Header("auth-token") token: String,
        @Path("id") registerId: String
    ): Response<BaseResponse<Any>>
}