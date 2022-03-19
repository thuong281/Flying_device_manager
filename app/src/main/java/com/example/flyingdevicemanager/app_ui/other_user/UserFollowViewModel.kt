package com.example.flyingdevicemanager.app_ui.other_user

import androidx.lifecycle.*
import com.example.flyingdevicemanager.models.UserFollow
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
    
    val searchUserLoading: MutableLiveData<Boolean> = MutableLiveData()
    
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
}