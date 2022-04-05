package com.example.flyingdevicemanager.repository

import androidx.paging.*
import androidx.paging.PagingConfig.Companion.MAX_SIZE_UNBOUNDED
import com.example.flyingdevicemanager.api.RetrofitInstance
import com.example.flyingdevicemanager.app_ui.add_device.history.paging.DevicePagingSource
import com.example.flyingdevicemanager.app_ui.user_profile.review_user_kyc.paging.UserPagingSource
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.util.base.BaseResponse
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
        return RetrofitInstance.api.getCurrentUserDevices(token)
    }
    
    suspend fun getUserDevices(
        token: String, userId: String
    ): Response<BaseResponse<List<Device>>> {
        return RetrofitInstance.api.getUserDevices(token, userId)
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
            pageSize = 10,
            maxSize = MAX_SIZE_UNBOUNDED,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { UserPagingSource(RetrofitInstance.api, token) }
    ).liveData
    
    suspend fun insertNewDevice(
        token: String, registerName: String, registerNationalId: String, registerPhone: String,
        devicePlate: String, deviceColor: String, deviceManufacturer: String, deviceBuyDate: String
    ): Response<BaseResponse<Any>> {
        return RetrofitInstance.api.insertNewDevice(
            token,
            registerName,
            registerNationalId,
            registerPhone,
            devicePlate,
            deviceColor,
            deviceManufacturer,
            deviceBuyDate
        )
    }
    
    fun getListAddDeviceHistory(token: String) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = MAX_SIZE_UNBOUNDED,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { DevicePagingSource(RetrofitInstance.api, token) }
    ).liveData
    
    suspend fun searchRegister(
        token: String, word: String
    ): Response<BaseResponse<List<Register>>> {
        return RetrofitInstance.api.searchRegister(token, word)
    }
    
    suspend fun searchDevice(token: String, word: String): Response<BaseResponse<List<Device2>>> {
        return RetrofitInstance.api.searchDevice(token, word)
    }
    
    suspend fun getDeviceById(token: String, id: String): Response<BaseResponse<Device2>> {
        return RetrofitInstance.api.getDeviceById(token, id)
    }
    
    suspend fun updateDevice(
        token: String, deviceId: String, devicePlate: String, deviceColor: String,
        deviceManufacturer: String, deviceBuyDate: String
    ): Response<BaseResponse<Any>> {
        return RetrofitInstance.api.updateDevice(
            token,
            deviceId,
            devicePlate,
            deviceColor,
            deviceManufacturer,
            deviceBuyDate
        )
    }
}