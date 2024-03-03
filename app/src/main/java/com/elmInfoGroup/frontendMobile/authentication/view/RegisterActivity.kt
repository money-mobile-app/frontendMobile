package com.elmInfoGroup.frontendMobile.authentication.view

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.elmInfoGroup.frontendMobile.R
import com.elmInfoGroup.frontendMobile.authentication.data.SignUpRequest
import com.elmInfoGroup.frontendMobile.authentication.data.ValidatePhoneBody
import com.elmInfoGroup.frontendMobile.authentication.repository.AuthRepository
import com.elmInfoGroup.frontendMobile.authentication.util.APIAuthentication
import com.elmInfoGroup.frontendMobile.authentication.util.RetrofitInstance
import com.elmInfoGroup.frontendMobile.authentication.view_model.RegisterActivityViewModel
import com.elmInfoGroup.frontendMobile.authentication.view_model.RegisterActivityViewModelFactory
import com.elmInfoGroup.frontendMobile.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(), View.OnKeyListener, View.OnFocusChangeListener,
    View.OnClickListener {
    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mViewModel: RegisterActivityViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        //create a Retrofit instance
        val retrofitInstance= RetrofitInstance
            .getRetrofitInstance()
            .create(APIAuthentication::class.java)

        mBinding.firstName.onFocusChangeListener = this
        mBinding.lastName.onFocusChangeListener = this
        mBinding.password.onFocusChangeListener = this
        mBinding.passwordConfirmation.onFocusChangeListener = this
        mBinding.phoneNumber.onFocusChangeListener = this

        mBinding.registerBtn.setOnClickListener(this)

        //initialize the view model
        mViewModel = ViewModelProvider(this,
            RegisterActivityViewModelFactory(AuthRepository(retrofitInstance),application)
        )
            .get(RegisterActivityViewModel::class.java)

        setupObservers()
    }

    // Set up a listener to detect changes in the selected country code


    private fun setupObservers(){
        mViewModel.getIsLoading().observe(this){
            mBinding.progressBar.isVisible = it
        }
        mViewModel.getIsUniquePhone().observe(this){
            if(validatePhoneNumber(shouldUpdateView = false)){
                if (!it){
                    mBinding.phoneNumberTil.apply {
                        isErrorEnabled = true
                        error = "phone already exist try to login"
                    }
                }
            }
        }
        mViewModel.getErrorMessage().observe(this){
                mBinding.phoneNumberTil.apply{
                    error = it
                }
        }
     mViewModel.getTOKEN().observe(this){
         if(it!=null){
             val intent = Intent(this, PinConfirmActivity::class.java)
             var phoneNumber = mBinding.phoneNumber.text.toString()
             val countryCode:String = gettingTheCountryCode()
             phoneNumber = countryCode.plus(phoneNumber)
             intent.putExtra("phone",phoneNumber)
             startActivity(intent)

         }
     }


    }



    private fun validateFirstName(): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.firstName.text.toString()
        if (value.isEmpty()) {
            errorMessage = "First Name is required"
        }
        if (errorMessage != null) {
            mBinding.firstName.apply {
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validateLastName(): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.lastName.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Last Name is required"
        }

        if (errorMessage != null) {
            mBinding.lastName.apply {
                error = errorMessage
            }
        }
        return errorMessage == null
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


    private fun validateConfirmPassword(): Boolean {
        var messageError: String? = null
        val value = mBinding.passwordConfirmation.text.toString()
        if (value.isEmpty()) {
            messageError = "Confirm password is required"
        } else if (value.length < 4) {
            messageError = "confirm password must be 4 digit long"
        }

        if (messageError != null) {
            mBinding.passwordConfTil.apply {
                isErrorEnabled = true
                error = messageError
            }
        }
        return messageError == null
    }

    private fun validatePasswordAndConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val password = mBinding.password.text.toString()
        val confirmPassword = mBinding.passwordConfirmation.text.toString()
        if (password != confirmPassword) {
            errorMessage = "Password and Confirm Password Doesn't Match"
        }

        if (errorMessage != null) {
            mBinding.passwordConfTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null

    }


    override fun onKey(view: View?, event: Int, keyEvent: KeyEvent?): Boolean {
        return false
    }


    override fun onClick(view: View?) {

        if (view != null) {
            if(view.id == R.id.registerBtn){
                onSubmit()
            }
        };

    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.firstName -> {
                    if (hasFocus) {
                        if (mBinding.firstNameTil.isErrorEnabled) {
                            mBinding.firstNameTil.isErrorEnabled = false
                        }

                    } else {
                        validateFirstName()
                    }
                }

                R.id.lastName -> {
                    if (hasFocus) {
                        if (mBinding.lastNameTil.isErrorEnabled) {
                            mBinding.lastNameTil.isErrorEnabled = false
                        }

                    } else {
                        validateLastName()
                    }
                }

                R.id.phoneNumber -> {
                    if (hasFocus) {
                        if (mBinding.phoneNumberTil.isErrorEnabled) {
                            mBinding.phoneNumberTil.isErrorEnabled = false
                        }
                    } else {
                        if (validatePhoneNumber()) {
                            // do validation for its uniqueness
                            mViewModel.validatePhoneNumberUniqueness(ValidatePhoneBody(mBinding.phoneNumber.text!!.toString()))
                        }
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

                R.id.passwordConfirmation -> {
                    if (hasFocus) {
                        if (mBinding.passwordConfTil.isErrorEnabled) {
                            mBinding.passwordConfTil.isErrorEnabled = false
                        }
                    } else {
                        validateConfirmPassword()
                        validatePasswordAndConfirmPassword()
                    }
                }
            }
        }
    }
    private fun  gettingTheCountryCode():String{
        var  country_code:kotlin.String? = mBinding.countryCode.selectedCountryCode
        // getting the country name
        var country_name: String = mBinding.countryCode.selectedCountryName
        // getting the country name code
        var country_namecode: String = mBinding.countryCode.selectedCountryNameCode
        return country_code.toString()
    }


    private fun onSubmit(){
        if(validate()){
            var fn = mBinding.firstName.text.toString()
            var ln = mBinding.lastName.text.toString()
            var phone = mBinding.phoneNumber.text.toString()
            val countryCode:String = gettingTheCountryCode()
            phone = countryCode.plus(phone)
            var pswrd = mBinding.password.text.toString()
            //api call
            mViewModel.registerUser(SignUpRequest(fn,ln,phone,pswrd))
        }
    }
    private fun validate():Boolean{
        var isValid = true
        if(!validateFirstName()) isValid = false
        if(!validateLastName()) isValid = false
        if(!validatePassword()) isValid = false
        if(!validateConfirmPassword()) isValid = false
        if(!validatePhoneNumber()) isValid = false
        return isValid
    }
}