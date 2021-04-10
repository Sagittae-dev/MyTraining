package com.example.mytraining

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_sign_up.*

var firebaseAuth = FirebaseAuth.getInstance()
var accountIsCreated = false

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        firebaseAuth = FirebaseAuth.getInstance()
        signUpButton?.setOnClickListener {
            if (!accountIsCreated) {
                signUp()
            }
        }
    }

    /*override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser
        updateUI(currentUser)
    }*/

    private fun updateUI(currentUser: FirebaseUser?) {

    }

    private fun signUp() {
        if(emailEditText.text.toString().isEmpty()){
            emailEditText.error = "Please enter mail"
            emailEditText.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()){
            emailEditText.error = "Invalid email"
            emailEditText.requestFocus()
            return
        }
        if (passwordEditText.text.toString().isEmpty()){
            passwordEditText.error = "Please enter password"
            passwordEditText.requestFocus()
            return
        }
        firebaseAuth.createUserWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    setUserName()
                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                //setUserName()
                                finish()
                                Toast.makeText(applicationContext, "Your verify mail is sent", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Authentication failed. This account already exists", Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }

    fun setUserName(){
        val user = firebaseAuth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(personNameEditText.text.toString())
            .build()
        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("UPDATE PROFILEEEEE", "User profile updated.")
                }
            }
    }

}