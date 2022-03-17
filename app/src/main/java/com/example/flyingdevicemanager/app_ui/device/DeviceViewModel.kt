package com.example.flyingdevicemanager.app_ui.device

import androidx.lifecycle.*
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.repository.Repository
import com.example.flyingdevicemanager.util.BaseResponse
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response

class DeviceViewModel : ViewModel() {
    
    private val repository: Repository by lazy { Repository() }
    
    val deviceList: MutableSharedFlow<Response<BaseResponse<List<Device>>>> = MutableSharedFlow()
    
    val userKycResponse: MutableSharedFlow<Response<BaseResponse<UserKyc>>> = MutableSharedFlow()
    
    val addDeviceResponse: MutableSharedFlow<Response<BaseResponse<Any>>> = MutableSharedFlow()
    
    val deleteDeviceResponse: MutableSharedFlow<Response<BaseResponse<Any>>> = MutableSharedFlow()
    
    val loadingList: MutableLiveData<Boolean> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    
    fun getCurrentUserDevices(token: String) {
        loadingList.postValue(true)
        viewModelScope.launch {
            val response = repository.getCurrentUserDevices(token)
            deviceList.emit(response)
            loadingList.postValue(false)
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
    
    fun addDevice(token: String, deviceName: String) {
        viewModelScope.launch {
            val response = repository.addDevice(token, deviceName)
            addDeviceResponse.emit(response)
        }
    }
    
    fun deleteDevice(token: String, deviceId: String) {
        viewModelScope.launch {
            val response = repository.deleteDevice(token, deviceId)
            deleteDeviceResponse.emit(response)
        }
    }
}