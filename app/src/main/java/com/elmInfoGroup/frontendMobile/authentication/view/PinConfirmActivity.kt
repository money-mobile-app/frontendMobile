package com.elmInfoGroup.frontendMobile.authentication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.elmInfoGroup.frontendMobile.MainActivity
import com.elmInfoGroup.frontendMobile.R
import com.elmInfoGroup.frontendMobile.authentication.data.OtpValidation
import com.elmInfoGroup.frontendMobile.authentication.repository.AuthRepository
import com.elmInfoGroup.frontendMobile.authentication.util.APIAuthentication
import com.elmInfoGroup.frontendMobile.authentication.util.RetrofitInstance
import com.elmInfoGroup.frontendMobile.authentication.view_model.PinConfirmActivityViewModel
import com.elmInfoGroup.frontendMobile.authentication.view_model.PinConfirmActivityViewModelFactory
import com.elmInfoGroup.frontendMobile.databinding.ActivityConfirmPinBinding

class PinConfirmActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var hBinding : ActivityConfirmPinBinding
    private lateinit var hViewModel: PinConfirmActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hBinding = ActivityConfirmPinBinding.inflate(LayoutInflater.from(this))
        setContentView(hBinding.root)


        hBinding.verfyPinBtn.setOnClickListener(this)
        //create a Retrofit instance
        val retrofitInstance = RetrofitInstance.getRetrofitInstance().create(APIAuthentication::class.java)

        //initialize the view model
        hViewModel = ViewModelProvider(this,
            PinConfirmActivityViewModelFactory(AuthRepository(retrofitInstance),application)
        )
            .get(PinConfirmActivityViewModel::class.java)

        // Retrieve the phone number from the Intent and show it
        extractPhoneNumberFromIntent()
        setupObservers()
    }
    private fun setupObservers(){

        hViewModel.getPinValidation().observe(this){
            if(it!=null){
                if(it.toString().equals("correct PIN code")){
                    startActivity(Intent(this@PinConfirmActivity,HomeActivity::class.java))
                }
                hBinding.confirmBinTil.apply {
                    error = it.toString()
                }
            }
        }
        hViewModel.getErrorMessage().observe(this){
            if(it!=null){
                hBinding.confirmBinTil.apply {
                    error = it.toString()
                }
            }
        }
    }

    private fun extractPhoneNumberFromIntent(){
        val phoneNumber = intent.getStringExtra("phone")
        if (phoneNumber != null) {
            val tokenTextView: TextView = hBinding.showPhoneNumberEt
            tokenTextView.text = phoneNumber
        }
    }

    private fun isValidPinCode(): Boolean {
        var errorMessage: String? = null
        val value: String = hBinding.codePin.text.toString()
        if (value.isEmpty()) {
            errorMessage = "code pin is required"
        } else if (value.length < 4) {
            errorMessage = "code pin must be 4 digit long"
        }

        if (errorMessage != null) {
            // message to show to user(error content)
            hBinding.confirmBinTil.apply {
                error = errorMessage
            }
        }

        return errorMessage == null
    }

    override fun onClick(view: View?) {
        if(view!= null){
            if(view.id == R.id.verfyPinBtn){
                submit()
            }
        }
    }

    private fun submit() {
        val phoneNumber = intent.getStringExtra("phone")
        val pin: String = hBinding.codePin.text.toString()
        if(isValidPinCode()){
            hViewModel.validatePinCode(OtpValidation(phoneNumber,pin))
        }
    }

}

