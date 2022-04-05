package com.example.flyingdevicemanager.app_ui.user_profile

import android.net.Uri
import androidx.lifecycle.*
import androidx.paging.*
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.repository.Repository
import com.example.flyingdevicemanager.util.base.BaseResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import retrofit2.Response

class UserViewModel : ViewModel() {
    
    private val repository: Repository by lazy { Repository() }
    
    val imageUri: MutableLiveData<Uri> = MutableLiveData(null)
    
    val getUserResponse: MutableSharedFlow<Response<BaseResponse<User>>> = MutableSharedFlow()
    
    val changeAvatarResponse: MutableSharedFlow<Response<BaseResponse<Any>>> = MutableSharedFlow()
    
    val getAllUserKycResponse: MutableSharedFlow<Response<BaseResponse<List<User>>>> =
        MutableSharedFlow()
    
    val getUserKycResponse: MutableSharedFlow<Response<BaseResponse<UserKyc>>> = MutableSharedFlow()
    
    val approveUserKycResponse: MutableSharedFlow<Response<BaseResponse<Any>>> = MutableSharedFlow()
    
    val rejectUserKycResponse: MutableSharedFlow<Response<BaseResponse<Any>>> = MutableSharedFlow()
    
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    
    val updateAvatarLoading: MutableLiveData<Boolean> = MutableLiveData()
    
    val listUserKycLoading: MutableLiveData<Boolean> = MutableLiveData()
    
    val userKycLoading: MutableLiveData<Boolean> = MutableLiveData()
    
    fun getUser(token: String) {
        viewModelScope.launch {
            loading.postValue(true)
            val response = repository.getCurrentUser(token)
            getUserResponse.emit(response)
            loading.postValue(false)
        }
    }
    
    fun changeAvatar(token: String, image: MultipartBody.Part) {
        viewModelScope.launch {
            updateAvatarLoading.postValue(true)
            val response = repository.updateAvatar(token, image)
            changeAvatarResponse.emit(response)
            updateAvatarLoading.postValue(false)
        }
    }
    
//    fun getAllUserKyc(token: String) {
//        viewModelScope.launch {
//            listUserKycLoading.postValue(true)
//            val response = repository.getAllUserKyc(token)
//            getAllUserKycResponse.emit(response)
//            listUserKycLoading.postValue(false)
//        }
//    }
    
    fun getUserKyc(token: String, userId: String) {
        viewModelScope.launch {
            userKycLoading.postValue(true)
            val response = repository.getOtherUserKyc(token, userId)
            getUserKycResponse.emit(response)
            userKycLoading.postValue(false)
        }
    }
    
    fun approveUserKyc(token: String, userId: String) {
        viewModelScope.launch {
            userKycLoading.postValue(true)
            val response = repository.approveUserKyc(token, userId)
            approveUserKycResponse.emit(response)
            userKycLoading.postValue(false)
        }
    }
    
    fun rejectUserKyc(token: String, userId: String) {
        viewModelScope.launch {
            userKycLoading.postValue(true)
            val response = repository.rejectUserKyc(token, userId)
            rejectUserKycResponse.emit(response)
            userKycLoading.postValue(false)
        }
    }
    
    var pagingFlow: LiveData<PagingData<User>> = MutableLiveData()
    
    fun getUserKycList(token: String) {
        pagingFlow = repository.getAllUserKycPaging(token).cachedIn(viewModelScope)
    }
    
}