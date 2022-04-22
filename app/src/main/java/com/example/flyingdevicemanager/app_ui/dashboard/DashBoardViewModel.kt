package com.example.flyingdevicemanager.app_ui.dashboard

import androidx.lifecycle.*
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.repository.Repository
import com.example.flyingdevicemanager.util.base.BaseResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class DashBoardViewModel: ViewModel() {
    private val repository: Repository by lazy { Repository() }
    
    val getSummaryResponse: MutableSharedFlow<Response<BaseResponse<CountSummary>>> = MutableSharedFlow()
    
    val getActiveDeviceResponse: MutableSharedFlow<Response<BaseResponse<List<Device2>>>> = MutableSharedFlow()
    
    val getAllDeviceResponse: MutableSharedFlow<Response<BaseResponse<List<Device2>>>> = MutableSharedFlow()
    
    val getAllRegisterResponse: MutableSharedFlow<Response<BaseResponse<List<Register>>>> = MutableSharedFlow()
    
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    
    val loadingActiveDevice: MutableLiveData<Boolean> = MutableLiveData()
    
    val loadingAllDevices: MutableLiveData<Boolean> = MutableLiveData()
    
    val loadingAllRegisters: MutableLiveData<Boolean> = MutableLiveData()
    
    fun getSummary(token: String) {
        viewModelScope.launch {
            loading.postValue(true)
            val response = repository.getSummary(token)
            getSummaryResponse.emit(response)
            loading.postValue(false)
        }
    }
    
    fun getActiveDevices(token: String) {
        viewModelScope.launch {
            loadingActiveDevice.postValue(true)
            val response = repository.getActiveDevices(token)
            getActiveDeviceResponse.emit(response)
            loadingActiveDevice.postValue(false)
        }
    }
    
    fun getAllDevices(token: String) {
        viewModelScope.launch {
            loadingAllDevices.postValue(true)
            val response = repository.getAllDevices(token)
            getAllDeviceResponse.emit(response)
            loadingAllDevices.postValue(false)
        }
    }
    
    fun getAllRegisters(token: String) {
        viewModelScope.launch {
            loadingAllRegisters.postValue(true)
            val response = repository.getAllRegisters(token)
            getAllRegisterResponse.emit(response)
            loadingAllRegisters.postValue(false)
        }
    }
    
}