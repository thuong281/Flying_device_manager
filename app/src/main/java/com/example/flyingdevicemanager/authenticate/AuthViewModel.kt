package com.example.flyingdevicemanager.authenticate

import android.app.Application
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.*
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.repository.Repository
import com.example.flyingdevicemanager.util.BaseResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.ConnectException

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: Repository by lazy { Repository() }
    
    
    val loginResponse: MutableLiveData<Response<BaseResponse<UserToken>>> = MutableLiveData()
    
    val signupResponse: MutableLiveData<Response<User>> = MutableLiveData()
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                loginResponse.value = response
            } catch (e: ConnectException) {
                Toast.makeText(getApplication(), "No internet connection", Toast.LENGTH_SHORT)
                    .show()
            }
            
        }
    }
    
    fun signup(email: String, userName: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.signUp(email, userName, password)
                signupResponse.value = response
            } catch (e: ConnectException) {
                Toast.makeText(getApplication(), "No internet connection", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}