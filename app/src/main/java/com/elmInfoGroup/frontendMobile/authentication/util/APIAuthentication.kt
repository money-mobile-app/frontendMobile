package com.elmInfoGroup.frontendMobile.authentication.util

import com.elmInfoGroup.frontendMobile.authentication.data.JwtAuthenticationResponse
import com.elmInfoGroup.frontendMobile.authentication.data.OtpValidation
import com.elmInfoGroup.frontendMobile.authentication.data.SignUpRequest
import com.elmInfoGroup.frontendMobile.authentication.data.SigningRequest
import com.elmInfoGroup.frontendMobile.authentication.data.ValidatePhoneBody
import com.elmInfoGroup.frontendMobile.authentication.data.CodePinMessage
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIAuthentication {
    @POST("api/authentication/validateNumberPhone-unique")
    suspend fun validatePhoneNumber(@Body body: ValidatePhoneBody): Response<Boolean>
    @POST("api/authentication/signup")
    suspend fun signupRequest(@Body body: SignUpRequest):Response<JwtAuthenticationResponse>
    @POST("api/authentication/signing")
    suspend fun signingRequest(@Body body: SigningRequest):Response<JwtAuthenticationResponse>
    @POST("api/authentication/otpCode")
    suspend fun validateOtpCode(@Body body: OtpValidation):Response<CodePinMessage>

}