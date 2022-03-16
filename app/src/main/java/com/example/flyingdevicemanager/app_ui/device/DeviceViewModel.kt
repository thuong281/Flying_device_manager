package com.example.flyingdevicemanager.app_ui.device

import androidx.lifecycle.*
import com.example.flyingdevicemanager.models.Device
import com.example.flyingdevicemanager.repository.Repository
import com.example.flyingdevicemanager.util.BaseResponse
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response

class DeviceViewModel : ViewModel() {
    
    private val repository: Repository by lazy { Repository() }
    
    val deviceList: MutableSharedFlow<Response<BaseResponse<List<Device>>>> = MutableSharedFlow()
    
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    
    fun getCurrentUserDevices(token: String) {
        loading.postValue(true)
        viewModelScope.launch {
            val response = repository.getCurrentUserDevices(token)
            deviceList.emit(response)
            loading.postValue(false)
        }
    }
}