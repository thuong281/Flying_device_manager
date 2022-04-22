package com.example.flyingdevicemanager.app_ui.add_device

import androidx.lifecycle.*
import androidx.paging.*
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.repository.Repository
import com.example.flyingdevicemanager.util.base.BaseResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class AddDeviceViewModel : ViewModel() {
    
    private val repository: Repository by lazy { Repository() }
    
    val addDeviceResponse: MutableSharedFlow<Response<BaseResponse<Any>>> = MutableSharedFlow()
    
    var pagingAddDeviceHistory: LiveData<PagingData<Device2>> = MutableLiveData()
    
    val loadingAddDevice: MutableLiveData<Boolean> = MutableLiveData()
    val loadingAddDeviceHistory: MutableLiveData<Boolean> = MutableLiveData()
    
    fun addDevice(
        token: String,isOrganization: Int, registerName: String, registerNationalId: String, registerPhone: String,
        devicePlate: String, deviceColor: String, deviceManufacturer: String, deviceBuyDate: String
    ) {
        viewModelScope.launch {
            loadingAddDevice.postValue(true)
            val response = repository.insertNewDevice(
                token,
                isOrganization,
                registerName,
                registerNationalId,
                registerPhone,
                devicePlate,
                deviceColor,
                deviceManufacturer,
                deviceBuyDate
            )
            addDeviceResponse.emit(response)
            loadingAddDevice.postValue(false)
        }
    }
    
    fun getAddDeviceHistoryList(token: String) {
        pagingAddDeviceHistory = repository.getListAddDeviceHistory(token).cachedIn(viewModelScope)
    }
}