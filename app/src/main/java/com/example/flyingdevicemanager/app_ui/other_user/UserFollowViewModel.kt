package com.example.flyingdevicemanager.app_ui.other_user

import androidx.lifecycle.*
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.repository.Repository
import com.example.flyingdevicemanager.util.BaseResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class UserFollowViewModel : ViewModel() {
    
    private val repository: Repository by lazy { Repository() }
    
    val searchUserResponse: MutableSharedFlow<Response<BaseResponse<List<UserFollow>>>> =
        MutableSharedFlow()
    
    val requestFollowResponse: MutableSharedFlow<Response<BaseResponse<Any>>> =
        MutableSharedFlow()
    
    val responseRequestFollowResponse: MutableSharedFlow<Response<BaseResponse<Any>>> =
        MutableSharedFlow()
    
    val listRequestFollowResponse: MutableSharedFlow<Response<BaseResponse<List<User>>>> = MutableSharedFlow()
    
    val listFollowingResponse: MutableSharedFlow<Response<BaseResponse<List<User>>>> = MutableSharedFlow()
    
    val searchUserLoading: MutableLiveData<Boolean> = MutableLiveData()
    val responseRequestFollowLoading: MutableLiveData<Boolean> = MutableLiveData()
    val getListRequestFollowLoading: MutableLiveData<Boolean> = MutableLiveData()
    val listFollowingLoading: MutableLiveData<Boolean> = MutableLiveData()
    
    fun searchUser(token: String, word: String) {
        searchUserLoading.postValue(true)
        viewModelScope.launch {
            val response = repository.searchUser(token, word)
            searchUserResponse.emit(response)
            searchUserLoading.postValue(false)
        }
    }
    
    fun requestFollow(token: String, userId: String) {
        viewModelScope.launch {
            searchUserLoading.postValue(true)
            val response = repository.requestFollow(token, userId)
            requestFollowResponse.emit(response)
            searchUserLoading.postValue(false)
        }
    }
    
    fun getListRequestFollow(token: String) {
        viewModelScope.launch {
            getListRequestFollowLoading.postValue(true)
            val response = repository.getListRequestFollow(token)
            listRequestFollowResponse.emit(response)
            getListRequestFollowLoading.postValue(false)
        }
    }
    
    fun responseRequestFollow(token: String, approve: Int, userRequestId: String) {
        viewModelScope.launch {
            responseRequestFollowLoading.postValue(true)
            val response = repository.responseRequestFollow(token, approve, userRequestId)
            responseRequestFollowResponse.emit(response)
            responseRequestFollowLoading.postValue(false)
        }
    }
    
    fun getListFollowing(token: String) {
        viewModelScope.launch {
            listFollowingLoading.postValue(true)
            val response = repository.getListFollow(token)
            listFollowingResponse.emit(response)
            listFollowingLoading.postValue(false)
        }
    }
}