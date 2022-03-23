package com.example.flyingdevicemanager.repository

import androidx.paging.*
import com.example.flyingdevicemanager.api.RetrofitInstance
import com.example.flyingdevicemanager.app_ui.user_profile.review_user_kyc.paging.UserPagingSource
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.util.BaseResponse
import okhttp3.*
import retrofit2.Response

class Repository {
    
    suspend fun login(email: String, password: String): Response<BaseResponse<UserToken>> {
        return RetrofitInstance.api.login(email, password)
    }
    
    suspend fun signUp(email: String, userName: String, password: String): Response<User> {
        return RetrofitInstance.api.signup(email, userName, password)
    }
    
    suspend fun submitUserKyc(
        token: String, image: List<MultipartBody.Part>, data: RequestBody
    ): Response<ApiMessage> {
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
    
    suspend fun responseRequestFollow(
        token: String, approve: Int, userRequestId: String
    ): Response<BaseResponse<Any>> {
        return RetrofitInstance.api.responseRequestFollow(token, approve, userRequestId)
    }
    
    suspend fun getListRequestFollow(token: String): Response<BaseResponse<List<User>>> {
        return RetrofitInstance.api.getListRequestFollow(token)
    }
    
    suspend fun getListFollow(token: String): Response<BaseResponse<List<User>>> {
        return RetrofitInstance.api.getListFollow(token)
    }
    
    suspend fun getDeviceLocation(
        token: String, deviceId: String
    ): Response<BaseResponse<Location>> {
        return RetrofitInstance.api.getDeviceLocation(token, deviceId)
    }
    
    suspend fun getCurrentUser(token: String): Response<BaseResponse<User>> {
        return RetrofitInstance.api.getCurrentUser(token)
    }
    
    suspend fun updateAvatar(
        token: String, image: MultipartBody.Part
    ): Response<BaseResponse<Any>> {
        return RetrofitInstance.api.updateAvatar(token, image)
    }
    
//    suspend fun getAllUserKyc(token: String): Response<BaseResponse<List<User>>> {
//        return RetrofitInstance.api.getAllUserKyc(token)
//    }
    
    suspend fun getOtherUserKyc(token: String, userId: String): Response<BaseResponse<UserKyc>> {
        return RetrofitInstance.api.getOtherUserKyc(token, userId)
    }
    
    suspend fun approveUserKyc(token: String, userId: String): Response<BaseResponse<Any>> {
        return RetrofitInstance.api.approveUserKyc(token, userId)
    }
    
    suspend fun rejectUserKyc(token: String, userId: String): Response<BaseResponse<Any>> {
        return RetrofitInstance.api.rejectUserKyc(token, userId)
    }
    
    fun getAllUserKycPaging(token: String) = Pager(
        config = PagingConfig(
            pageSize = 2,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { UserPagingSource(RetrofitInstance.api, token) }
    ).liveData
    
}