package com.elmInfoGroup.frontendMobile.authentication.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elmInfoGroup.frontendMobile.authentication.repository.AuthRepository
import java.security.InvalidParameterException

class PinConfirmActivityViewModelFactory(private val authRepository: AuthRepository, private val application: Application)
    :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PinConfirmActivityViewModel::class.java)) {
            return PinConfirmActivityViewModel(authRepository,application) as T
        }
        throw InvalidParameterException("Unable to construct PinConfirmActivityViewModel")

    }
}