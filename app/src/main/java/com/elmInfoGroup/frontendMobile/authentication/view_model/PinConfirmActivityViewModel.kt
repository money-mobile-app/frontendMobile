package com.elmInfoGroup.frontendMobile.authentication.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmInfoGroup.frontendMobile.authentication.data.CodePinMessage
import com.elmInfoGroup.frontendMobile.authentication.data.OtpValidation
import com.elmInfoGroup.frontendMobile.authentication.repository.AuthRepository
import com.elmInfoGroup.frontendMobile.authentication.util.RequestStatus
import kotlinx.coroutines.launch

class PinConfirmActivityViewModel(private val authRepository: AuthRepository, private val application: Application):ViewModel() {
    private var isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }
    private var errorMessage: MutableLiveData<String> = MutableLiveData()
   private var validPinMsg: MutableLiveData<String> = MutableLiveData()

    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getErrorMessage(): LiveData<String> = errorMessage
    fun getPinValidation():LiveData<String> = validPinMsg



    fun validatePinCode(body: OtpValidation){
        viewModelScope.launch {
            authRepository.validateCodePin(body).collect{
                when(it){
                    is RequestStatus.Waiting ->{
                        isLoading.value = true
                    }
                    is RequestStatus.Success ->{
                        isLoading.value = false
                        validPinMsg.value = it.data.pinMessage

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