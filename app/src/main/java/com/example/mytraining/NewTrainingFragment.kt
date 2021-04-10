package com.example.mytraining

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.mytraining.MainActivity.Companion.applicationContext
import com.example.mytraining.newtraining.TrainingActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_new_training.*

class NewTrainingFragment : Fragment() {

    private var bodyWeight: Double = 0.0

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Toast.makeText(applicationContext(), "name:"+ firebaseAuth.currentUser?.displayName.toString(), Toast.LENGTH_SHORT).show()
        if(firebaseAuth.currentUser != null) {
            setLookOfNewTrainingFragmentWhenUserIsLoggedIn()
        }else {
            setLookOfNewTrainingFragmentWhenUserIsLoggedOut()
            }

        newTrainingFragmentConstraintLayout.setOnClickListener {
            closeKeyboard()
        }

        yourWeightEditTextNumberDecimal?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(4))
        yourWeightEditTextNumberDecimal?.doOnTextChanged { text, start, count, after ->
            startNewTrainingButton.isEnabled = text.toString().length > 1
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(firebaseAuth.currentUser != null) {
            setLookOfNewTrainingFragmentWhenUserIsLoggedIn()
        }else {
            setLookOfNewTrainingFragmentWhenUserIsLoggedOut()
        }
        return inflater.inflate(R.layout.fragment_new_training, container, false)
    }

    override fun onResume() {
        super.onResume()
        if(firebaseAuth.currentUser != null) {
            setLookOfNewTrainingFragmentWhenUserIsLoggedIn()
        }else {
            setLookOfNewTrainingFragmentWhenUserIsLoggedOut()
        }
    }

    /*@SuppressLint("CommitPrefEdits")
    private fun saveNameAndWeightBySharPref() {
        bodyWeight = yourWeightEditTextNumberDecimal.text.toString().toFloat()
        val pref: SharedPreferences = this.activity!!.getSharedPreferences("nameAndWeight", AppCompatActivity.MODE_PRIVATE)
        val prefEdit = pref.edit()
        prefEdit.putFloat("weight", bodyWeight)
        prefEdit.apply()
    }*/

    @SuppressLint("SetTextI18n")
    private fun setLookOfNewTrainingFragmentWhenUserIsLoggedOut() {
        titleNewTrainingTextView?.text = "Welcome in My Training"
        WEIGHT?.visibility = View.GONE
        KG?.visibility = View.GONE
        aboutAppTextView?.visibility = View.VISIBLE
        yourWeightEditTextNumberDecimal?.visibility = View.GONE
        startNewTrainingButton?.isEnabled = true
        startNewTrainingButton?.setOnClickListener {
            (activity as MainActivity).setFragment(2)
        }
    }
    @SuppressLint("SetTextI18n")
    private fun setLookOfNewTrainingFragmentWhenUserIsLoggedIn(){
        titleNewTrainingTextView?.text = "Hi, "+ firebaseAuth.currentUser?.displayName.toString() + " welcome in MyTraining!"
        WEIGHT?.visibility = View.VISIBLE
        KG?.visibility = View.VISIBLE
        aboutAppTextView?.visibility = View.GONE
        changeWeightButton?.visibility = View.VISIBLE
        yourWeightEditTextNumberDecimal?.visibility = View.VISIBLE
        startNewTrainingButton?.setOnClickListener {
            startNewTraining()
        }
        changeWeightButton?.setOnClickListener {
            changeWeightInFirebase()
        }
        completeWeightFieldFromFirebase()
    }

    private fun changeWeightInFirebase() {
        val weight = yourWeightEditTextNumberDecimal?.editableText.toString()
        Log.i("Weigt == ", weight)
        FirebaseDatabase.getInstance().getReference(firebaseAuth.currentUser!!.uid).child("weight").setValue(weight)
        bodyWeight = weight.toDouble()
    }

    @SuppressLint("SetTextI18n")
    private fun startNewTraining(){
        //saveNameAndWeightBySharPref()
        val intent= Intent(applicationContext(), TrainingActivity::class.java)
        intent.putExtra("weight", bodyWeight)
        startActivity(intent)
        startNewTrainingButton.text = "Back to training"
        startNewTrainingButton.isEnabled = true
    }

    private fun completeWeightFieldFromFirebase() {

        val reference = FirebaseDatabase.getInstance().getReference(firebaseAuth.currentUser!!.uid)
        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.child("weight").value?.toString()
                if (!data.equals("") && data !=null) {
                    bodyWeight = data.toDouble()
                    yourWeightEditTextNumberDecimal?.setText(data)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun closeKeyboard() {
        val activity = activity as MainActivity
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}