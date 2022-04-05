package com.example.flyingdevicemanager.app_ui.user_profile.user_kyc

import android.net.Uri
import androidx.lifecycle.*
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.repository.Repository
import com.example.flyingdevicemanager.util.base.BaseResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import okhttp3.*
import retrofit2.Response


class UserKycViewModel : ViewModel() {
    
    val frontImageUri: MutableLiveData<Uri> = MutableLiveData()
    val backImageUri: MutableLiveData<Uri> = MutableLiveData()
    
    val submitResponse: MutableSharedFlow<Response<ApiMessage>> = MutableSharedFlow()
    
    val userKycResponse: MutableSharedFlow<Response<BaseResponse<UserKyc>>> = MutableSharedFlow()
    
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    
    private val repository: Repository by lazy { Repository() }
    
    fun submitUserKyc(token: String, image: List<MultipartBody.Part>, data: RequestBody) {
        loading.postValue(true)
        viewModelScope.launch {
            val response = repository.submitUserKyc(token, image, data)
            submitResponse.emit(response)
            loading.postValue(false)
        }
    }
    
    fun getUserKyc(token: String) {
        loading.postValue(true)
        viewModelScope.launch {
            val response = repository.getUserKyc(token)
            userKycResponse.emit(response)
            loading.postValue(false)
        }
    }
}