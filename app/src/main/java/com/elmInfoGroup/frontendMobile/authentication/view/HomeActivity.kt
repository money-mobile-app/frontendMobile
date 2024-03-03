package com.elmInfoGroup.frontendMobile.authentication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.elmInfoGroup.frontendMobile.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val list = arrayOf(
        "USD",
        "USD1",
        "USD2"
    )
    private lateinit var hBinding: ActivityHomeBinding
    lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hBinding = ActivityHomeBinding.inflate(LayoutInflater.from(this))
        setContentView(hBinding.root)

        // Initialize Spinner
        hBinding.spinnerCorrency.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, list
        )
        hBinding.spinnerCorrency.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedItem = parent?.getItemAtPosition(position) as String
        // Handle the selected item as needed
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Handle the case where nothing is selected
    }
}
