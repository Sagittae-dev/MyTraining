package com.example.mytraining.newtraining

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doOnTextChanged
import com.example.mytraining.R
import com.example.mytraining.Training
import com.example.mytraining.newtraining.TrainingActivity.Companion.trainingContext
import com.example.mytraining.traininghistory.TrainingHistoryFragment
import kotlinx.android.synthetic.main.activity_new_series.*


class NewSeriesActivity : AppCompatActivity() {

    var weight: Double = 0.0
    var reps: Int = 0
    var volume: Double = 0.0
    //var volumeApproximate: Double = 0.0
    var annotation: String = ""
    var seriesInstance: NewSeriesActivity? = null
    var oneRepMax: Double = 0.0
    lateinit var trainingHistoryList: MutableList<Training>
    init {
        seriesInstance = this
    }
            fun hideKeyboard() {
                val view =  this.currentFocus
                if(view != null) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                //else
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_series)
        val trainingHistoryFragment = TrainingHistoryFragment()
        trainingHistoryList = trainingHistoryFragment.getTrainingHistoryList()
        Log.i("NewSeries", "traininghistory size: "+ trainingHistoryList.size)
        setYourWeightButton.setOnClickListener {
            val pref: SharedPreferences = getSharedPreferences("nameAndWeight",MODE_PRIVATE)
            val bodyWeight = pref.getFloat("weight", 0.0f)
            weightEditTextNumber.setText(bodyWeight.toString())
        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        submitSerieButton.isEnabled = false

        newSeriesConstraintLayout.setOnClickListener {
            hideKeyboard()
        }

        setEditableTextFieldsLength()

        goBackToTrainingButton.setOnClickListener {
            seriesInstance?.finish()
        }

        weightEditTextNumber.doOnTextChanged { text, start, count, after ->
            showVolume()
        }

        repsEditTextNumber.doOnTextChanged { text, start, count, after ->
            showVolume()
        }
        submitSerieButton.setOnClickListener {
            submitSerie()
        }
    }

    private fun setEditableTextFieldsLength() {

        weightEditTextNumber.filters = arrayOf<InputFilter>(LengthFilter(3))
        repsEditTextNumber.filters = arrayOf<InputFilter>(LengthFilter(2))
        annotationEditText.filters = arrayOf<InputFilter>(LengthFilter(15))
    }

    private fun submitSerie() {
        val intent = Intent(trainingContext(), TrainingActivity::class.java)
        annotation = annotationEditText?.text.toString()
        intent.putExtra("weight", weight)
        intent.putExtra("reps", reps)
        intent.putExtra("volume", volume)
        intent.putExtra("annotation", annotation)
        seriesInstance?.setResult(Activity.RESULT_OK,intent.putExtra("weight", weight))
        seriesInstance!!.finish()
    }

    @SuppressLint("SetTextI18n")
    private fun showVolume(){

        if (repsEditTextNumber.text.toString() != "" && weightEditTextNumber.text.toString() != "") {
            submitSerieButton.isEnabled = true
            weight =  weightEditTextNumber.text.toString().toDouble()
            reps = repsEditTextNumber.text.toString().toInt()
           // if (weight != null && reps != null) {
                if ((weight * reps) >= 0.0) {
                    volume = (weight * reps)
                    oneRepMax = (weight/(1.0278 - (0.0278 * reps)))
                    val number3digits:Double = Math.round(oneRepMax * 1000.0) / 1000.0
                    val number2digits:Double = Math.round(number3digits * 100.0) / 100.0
                    val onerepMaxSolution:Double = Math.round(number2digits * 10.0) / 10.0

                    val volume3digits:Double = Math.round(volume * 1000.0) / 1000.0
                    val volume2digits:Double = Math.round(volume3digits * 100.0) / 100.0
                    volume = Math.round(volume2digits * 10.0) / 10.0

                    volumeTextView.text = "Volume= $volume"
                    oneRepMaxTextView.text = "Your one rep max (1RM) ≈ $onerepMaxSolution"
                }
           // }
        }else{
            oneRepMaxTextView.text = "Your one rep max (1RM) ≈"
            volumeTextView.text = "Volume = 0"
            submitSerieButton.isEnabled = false
        }
    }
}
