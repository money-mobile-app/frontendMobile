package com.elmInfoGroup.frontendMobile.authentication.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmInfoGroup.frontendMobile.authentication.data.JwtAuthenticationResponse
import com.elmInfoGroup.frontendMobile.authentication.data.SignUpRequest
import com.elmInfoGroup.frontendMobile.authentication.data.ValidatePhoneBody
import com.elmInfoGroup.frontendMobile.authentication.repository.AuthRepository
import com.elmInfoGroup.frontendMobile.authentication.util.AuthToken
import com.elmInfoGroup.frontendMobile.authentication.util.RequestStatus
import kotlinx.coroutines.launch

class RegisterActivityViewModel(private val authRepository: AuthRepository, private val application: Application) :
    ViewModel() {
    private var isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }
    private var errorMessage: MutableLiveData<String> = MutableLiveData()
    private var isUniquePhone: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }

    private var token: MutableLiveData<JwtAuthenticationResponse?> = MutableLiveData()

    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getErrorMessage(): LiveData<String> = errorMessage
    fun getIsUniquePhone(): LiveData<Boolean> = isUniquePhone
    fun getTOKEN(): LiveData<JwtAuthenticationResponse?> = token

    fun validatePhoneNumberUniqueness(body: ValidatePhoneBody) {
        viewModelScope.launch {
            authRepository.validatePhoneNumberUniqueness(body).collect{
                when(it){
                    is RequestStatus.Waiting ->{
                        isLoading.value = true
                    }
                    is RequestStatus.Success ->{
                        isLoading.value = false
                        isUniquePhone.value= it.data == true
                    }
                    is RequestStatus.Error ->{
                        isLoading.value = false
                        errorMessage.value = it.message
                    }
                }
            }
        }

    }

    fun registerUser(body: SignUpRequest){
        viewModelScope.launch {
            authRepository.registerUser(body).collect{
                when(it){
                    is RequestStatus.Waiting ->{
                        isLoading.value = true
                    }
                    is RequestStatus.Success ->{
                        isLoading.value = false
                        token.value = it.data
                        //save toke to shared preferences
                        AuthToken.getInstance(application.baseContext).token = it.data.token
                    }
                    is RequestStatus.Error ->{
                        isLoading.value = false
                        errorMessage.value = it.message
                    }
                }
            }
        }
    }

}