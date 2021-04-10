package com.example.mytraining.account

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.mytraining.R

class AccountSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)
        setLayout()
    }

    private fun setLayout() {
        setTypeOfTrainingSpinner()
    }

    private fun setTypeOfTrainingSpinner() {
        val spinner: Spinner = findViewById(R.id.favoriteTypeOfTrainingSpinner)
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_item, resources.getStringArray(R.array.typeOfTrainings))
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = adapter
        val items = resources.getStringArray(R.array.typeOfTrainings)
            spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.i("spinner", items[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

}