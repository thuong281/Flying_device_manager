package com.example.flyingdevicemanager.app_ui.user_profile

import android.net.Uri
import androidx.lifecycle.*
import com.example.flyingdevicemanager.models.User
import com.example.flyingdevicemanager.repository.Repository
import com.example.flyingdevicemanager.util.BaseResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response

class UserViewModel : ViewModel() {
    
    private val repository: Repository by lazy { Repository() }
    
    val imageUri: MutableLiveData<Uri> = MutableLiveData(null)
    
    val getUserResponse: MutableSharedFlow<Response<BaseResponse<User>>> = MutableSharedFlow()
    
    val changeAvatarResponse: MutableSharedFlow<Response<BaseResponse<Any>>> = MutableSharedFlow()
    
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    
    val updateAvatarLoading: MutableLiveData<Boolean> = MutableLiveData()
    
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
}