package com.elmInfoGroup.frontendMobile.authentication.repository

import com.elmInfoGroup.frontendMobile.authentication.data.OtpValidation
import com.elmInfoGroup.frontendMobile.authentication.data.SignUpRequest
import com.elmInfoGroup.frontendMobile.authentication.data.SigningRequest
import com.elmInfoGroup.frontendMobile.authentication.data.ValidatePhoneBody
import com.elmInfoGroup.frontendMobile.authentication.util.APIAuthentication
import com.elmInfoGroup.frontendMobile.authentication.util.RequestStatus
import kotlinx.coroutines.flow.flow

class AuthRepository(private val apiAuthRepository: APIAuthentication) {

    fun validatePhoneNumberUniqueness(body: ValidatePhoneBody) = flow {
           emit(RequestStatus.Waiting)

            val response = apiAuthRepository.validatePhoneNumber(body)
            if (response.isSuccessful) {
                emit(RequestStatus.Success(response.body()!!))
            } else {
                emit(RequestStatus.Error("Phone number already exists"))
            }

    }

    fun registerUser(body: SignUpRequest)= flow {
        emit(RequestStatus.Waiting)

        val response = apiAuthRepository.signupRequest(body)
        if (response.isSuccessful) {
            emit(RequestStatus.Success(response.body()!!))
        } else {
            emit(RequestStatus.Error("Register Failed"))
        }
    }

    fun loginUser(body: SigningRequest)= flow {
        emit(RequestStatus.Waiting)

        val response = apiAuthRepository.signingRequest(body)
        if (response.isSuccessful) {
            emit(RequestStatus.Success(response.body()!!))
        } else {
            emit(RequestStatus.Error("Register Failed"))
        }
    }

    fun validateCodePin(body: OtpValidation)= flow {
        val response = apiAuthRepository.validateOtpCode(body)
        if (response.isSuccessful) {
            emit(RequestStatus.Success(response.body()!!))
        } else {
            emit(RequestStatus.Error("Register Failed"))
        }
    }

}
