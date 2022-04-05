package com.example.flyingdevicemanager.app_ui.search_device

import androidx.lifecycle.*
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.repository.Repository
import com.example.flyingdevicemanager.util.base.BaseResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchViewModel : ViewModel() {
    private val repository: Repository by lazy { Repository() }
    
    val deviceListResponse: MutableSharedFlow<Response<BaseResponse<List<Device2>>>> =
        MutableSharedFlow()
    
    val registerListResponse: MutableSharedFlow<Response<BaseResponse<List<Register>>>> =
        MutableSharedFlow()
    
    val getDeviceByIdResponse: MutableSharedFlow<Response<BaseResponse<Device2>>> =
        MutableSharedFlow()
    
    val updateDeviceResponse: MutableSharedFlow<Response<BaseResponse<Any>>> = MutableSharedFlow()
    
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    
    val deviceDetailLoading: MutableLiveData<Boolean> = MutableLiveData()
    
    val updateDeviceLoading: MutableLiveData<Boolean> = MutableLiveData()
    
    fun searchDevice(token: String, word: String) {
        loading.postValue(true)
        viewModelScope.launch {
            val response = repository.searchDevice(token, word)
            deviceListResponse.emit(response)
            loading.postValue(false)
        }
    }
    
    fun searchRegister(token: String, word: String) {
        loading.postValue(true)
        viewModelScope.launch {
            val response = repository.searchRegister(token, word)
            registerListResponse.emit(response)
            loading.postValue(false)
        }
    }
    
    fun getDeviceById(token: String, id: String) {
        deviceDetailLoading.postValue(true)
        viewModelScope.launch {
            val response = repository.getDeviceById(token, id)
            getDeviceByIdResponse.emit(response)
            deviceDetailLoading.postValue(false)
        }
    }
    
    fun updateDevice(
        token: String, deviceId: String, devicePlate: String, deviceColor: String,
        deviceManufacturer: String, deviceBuyDate: String
    ) {
        updateDeviceLoading.postValue(true)
        viewModelScope.launch {
            val response = repository.updateDevice(
                token, deviceId, devicePlate, deviceColor,
                deviceManufacturer, deviceBuyDate
            )
            updateDeviceResponse.emit(response)
            updateDeviceLoading.postValue(false)
        }
    }
}