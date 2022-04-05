package com.example.flyingdevicemanager.app_ui.map

import androidx.lifecycle.*
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.repository.Repository
import com.example.flyingdevicemanager.util.base.BaseResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class MapsViewModel : ViewModel() {
    private val repository: Repository by lazy { Repository() }
    
    val locationResponse: MutableSharedFlow<Response<BaseResponse<Location>>> = MutableSharedFlow()
    
    fun getDeviceLocation(token: String, deviceId: String) {
        viewModelScope.launch {
            val response = repository.getDeviceLocation(token, deviceId)
            locationResponse.emit(response)
        }
    }
}