package com.elmInfoGroup.frontendMobile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.elmInfoGroup.frontendMobile.authentication.view.LoginActivity
import com.elmInfoGroup.frontendMobile.authentication.view.RegisterActivity
import com.elmInfoGroup.frontendMobile.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() ,View.OnClickListener{
    private lateinit var mBinding: ActivityMainBinding
    private  var lang_selected:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)


        mBinding.showlangdialog.setOnClickListener(this)
        mBinding.signupBtn.setOnClickListener(this)
        mBinding.loginBtn.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        if (view != null) {
            if(view.id == R.id.showlangdialog){
                val Languages = arrayOf("ENGLISH", "FRANCAIS")
                val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                if (view.id == R.id.showlangdialog) {
                    builder.setTitle("SELECT LANGUAGE")
                        .setSingleChoiceItems(Languages, lang_selected) { dialog, which ->
                            mBinding.dialogLanguage.setText(Languages[which])
                            if(Languages[which].equals("ENGLISH")){
                                setLocale(this@MainActivity,"en")
                            }
                            if(Languages[which].equals("FRANCAIS")){
                                setLocale(this@MainActivity,"fr")
                                mBinding.signupBtn.text = getString(R.string.signup)
                                mBinding.loginBtn.text = getString(R.string.login)
                            }
                        }
                        .setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
                        .create().show()
                }
            }


            if(view.id == R.id.signup_btn){
                val signupIntent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(signupIntent)
            }

            if(view.id == R.id.login_btn){
                val loginIntent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(loginIntent)
            }

        }

    }

    private fun setLocale(activity: Activity, langCode: String) {
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val resources = activity.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

    }


}