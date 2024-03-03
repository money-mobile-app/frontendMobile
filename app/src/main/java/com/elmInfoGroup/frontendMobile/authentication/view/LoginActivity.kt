package com.elmInfoGroup.frontendMobile.authentication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.elmInfoGroup.frontendMobile.R
import com.elmInfoGroup.frontendMobile.authentication.data.SigningRequest
import com.elmInfoGroup.frontendMobile.authentication.repository.AuthRepository
import com.elmInfoGroup.frontendMobile.authentication.util.APIAuthentication
import com.elmInfoGroup.frontendMobile.authentication.util.RetrofitInstance
import com.elmInfoGroup.frontendMobile.authentication.view_model.LoginActivityViewModel
import com.elmInfoGroup.frontendMobile.authentication.view_model.LoginActivityViewModelFactory
import com.elmInfoGroup.frontendMobile.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(),View.OnClickListener,View.OnFocusChangeListener {
    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var mViewModel: LoginActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        mBinding.loginBtn.setOnClickListener(this)
        mBinding.linkSignUp.setOnClickListener(this)
        mBinding.password.onFocusChangeListener= this
        mBinding.phoneNumber.onFocusChangeListener = this

        //create a Retrofit instance
        val retrofitInstance= RetrofitInstance
            .getRetrofitInstance()
            .create(APIAuthentication::class.java)
        mViewModel = ViewModelProvider(this,
            LoginActivityViewModelFactory(AuthRepository(retrofitInstance),application)
        ).get(LoginActivityViewModel::class.java)

        setupObservers()
    }

    private fun setupObservers(){
//        mViewModel.getIsLoading().observe(this){
////            mBinding.progressBar.isVisible = it
//        }

        mViewModel.getErrorMessage().observe(this){
            mBinding.phoneNumberTil.apply{
                error = it
            }
        }
        mViewModel.getTOKEN().observe(this){
            if(it!=null){
                val intent = Intent(this, HomeActivity::class.java)
                var phoneNumber = mBinding.phoneNumber.text.toString()
                intent.putExtra("phone",phoneNumber)
                startActivity(intent)

            }
        }


    }
    private fun validatePhoneNumber(shouldUpdateView:Boolean = true): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.phoneNumber.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Phone Number is required"
        } else if (!Patterns.PHONE.matcher(value).matches()) {
            errorMessage = "Phone Number is invalid"
        }

        if (errorMessage != null && shouldUpdateView) {
            mBinding.phoneNumberTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validatePassword(): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.password.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Password is required"
        } else if (value.length < 4) {
            errorMessage = "Password must be 4 digit long"
        }

        if (errorMessage != null) {
            // message to show to user(error content)
            mBinding.passwordTil.apply {
                error = errorMessage
            }
        }

        return errorMessage == null
    }

    override fun onClick(view: View?) {
       if(view != null){
           when(view.id){
               R.id.loginBtn ->{
                    submitForm()
               }
               R.id.linkSignUp ->{
                   startActivity(Intent(this, RegisterActivity::class.java))
               }
           }
       }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.phoneNumber -> {
                    if (hasFocus) {
                        if (mBinding.phoneNumberTil.isErrorEnabled) {
                            mBinding.phoneNumberTil.isErrorEnabled = false
                        }
                    } else {
                        validatePhoneNumber()
                    }
                }

                R.id.password -> {
                    if (hasFocus) {
                        if (mBinding.passwordTil.isErrorEnabled) {
                            mBinding.passwordTil.isErrorEnabled = false
                        }

                    } else {
                        validatePassword()
                    }
                }

            }
        }
    }

    fun submitForm(){
       if(validatePhoneNumber() && validatePassword()){
           val phone:String = mBinding.phoneNumber.text.toString()
           val password:String = mBinding.password.text.toString()
           mViewModel.loginUser(SigningRequest(phone,password))
       }
    }


}