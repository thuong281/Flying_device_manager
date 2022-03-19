package com.example.flyingdevicemanager.repository

import com.example.flyingdevicemanager.api.RetrofitInstance
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.util.BaseResponse
import com.google.gson.JsonObject
import okhttp3.*
import org.json.JSONObject
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class Repository {
    
    suspend fun login(email: String, password: String): Response<BaseResponse<UserToken>> {
        return RetrofitInstance.api.login(email, password)
    }
    
    suspend fun signUp(email: String, userName: String, password: String): Response<User> {
        return RetrofitInstance.api.signup(email, userName, password)
    }
    
    suspend fun submitUserKyc(token: String, image: List<MultipartBody.Part>, data: RequestBody): Response<ApiMessage> {
        return RetrofitInstance.api.requestKyc(token, image as ArrayList<MultipartBody.Part>, data)
    }
    
    suspend fun getUserKyc(token: String): Response<BaseResponse<UserKyc>> {
        return RetrofitInstance.api.getUserKyc(token)
    }
    
    suspend fun getCurrentUserDevices(token: String): Response<BaseResponse<List<Device>>> {
        return RetrofitInstance.api.getUserDevices(token)
    }
    
    suspend fun addDevice(token: String, deviceName: String): Response<BaseResponse<Any>> {
        return RetrofitInstance.api.addDevice(token, deviceName)
    }
    
    suspend fun deleteDevice(token: String, deviceID: String): Response<BaseResponse<Any>> {
        return RetrofitInstance.api.deleteDevice(token, deviceID)
    }
    
    suspend fun searchUser(token: String, word: String): Response<BaseResponse<List<UserFollow>>> {
        return RetrofitInstance.api.searchUser(token, word)
    }
    
    suspend fun requestFollow(token: String, userId: String): Response<BaseResponse<Any>> {
        return RetrofitInstance.api.requestFollow(token, userId)
    }
    
}