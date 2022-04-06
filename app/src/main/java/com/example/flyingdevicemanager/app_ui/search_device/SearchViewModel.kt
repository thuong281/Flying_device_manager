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
    
    val deleteDeviceResponse: MutableSharedFlow<Response<BaseResponse<Any>>> = MutableSharedFlow()
    
    val getRegisterByIdResponse: MutableSharedFlow<Response<BaseResponse<Register>>> = MutableSharedFlow()
    
    val getListDevicesResponse: MutableSharedFlow<Response<BaseResponse<List<Device2>>>> = MutableSharedFlow()
    
    val updateRegisterResponse: MutableSharedFlow<Response<BaseResponse<Any>>> = MutableSharedFlow()
    
    val deleteRegisterResponse: MutableSharedFlow<Response<BaseResponse<Any>>> = MutableSharedFlow()
    
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    
    val deviceDetailLoading: MutableLiveData<Boolean> = MutableLiveData()
    
    val registerDetailLoading: MutableLiveData<Boolean> = MutableLiveData()
    
    val updateDeviceLoading: MutableLiveData<Boolean> = MutableLiveData()
    
    val updateRegisterLoading: MutableLiveData<Boolean> = MutableLiveData()
    
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
    
    fun deleteDevice(token: String, deviceId: String) {
        deviceDetailLoading.postValue(true)
        viewModelScope.launch {
            val response = repository.deleteDevice(token, deviceId)
            deleteDeviceResponse.emit(response)
            deviceDetailLoading.postValue(false)
        }
    }
    
    fun getRegisterById(token: String, id: String) {
        registerDetailLoading.postValue(true)
        viewModelScope.launch {
            val response = repository.getRegister(token, id)
            getRegisterByIdResponse.emit(response)
            registerDetailLoading.postValue(false)
        }
    }
    
    fun getListDevices(token: String, id: String) {
        registerDetailLoading.postValue(true)
        viewModelScope.launch {
            val response = repository.getListDevices(token, id)
            getListDevicesResponse.emit(response)
            registerDetailLoading.postValue(false)
        }
    }
    
    fun updateRegister(token: String, registerId: String, name: String, nationalId: String, phoneNumber: String) {
        updateRegisterLoading.postValue(true)
        viewModelScope.launch {
            val response = repository.updateRegister(token, registerId, name, nationalId, phoneNumber)
            updateRegisterResponse.emit(response)
            updateRegisterLoading.postValue(false)
        }
    }
    
    fun deleteRegister(token: String, registerId: String) {
        registerDetailLoading.postValue(true)
        viewModelScope.launch {
            val response = repository.deleteRegister(token, registerId)
            deleteRegisterResponse.emit(response)
            registerDetailLoading.postValue(false)
        }
    }
}