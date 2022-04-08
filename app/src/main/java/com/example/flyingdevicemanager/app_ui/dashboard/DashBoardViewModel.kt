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
    
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    
    fun getSummary(token: String) {
        viewModelScope.launch {
            loading.postValue(true)
            val response = repository.getSummary(token)
            getSummaryResponse.emit(response)
            loading.postValue(false)
        }
    }
    
}