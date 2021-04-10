package com.example.mytraining.account

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.mytraining.MainActivity
import com.example.mytraining.MainActivity.Companion.applicationContext
import com.example.mytraining.R
import com.example.mytraining.SignUpActivity
import com.example.mytraining.firebaseAuth
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_account.*
import java.io.File

class AccountFragment : Fragment() {
    val fDatabase = FirebaseDatabase.getInstance()
    var storageReference: StorageReference = FirebaseStorage.getInstance().reference
    val localFIle = File.createTempFile("images", "jpg")
    companion object{
        val IMAGE_PICK_CODE = 1000
        val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profilePic.setImageURI(localFIle.toUri())
        if (firebaseAuth.currentUser != null){
            setLookOfAccountFragmentWhenUserIsLoggedIn()
        }
        else {
            Log.i("account", "user is logged out")
            setLookOfAccountFragmentWhenUserIsLoggedOut()
        }
        dontHaveAccountButton.setOnClickListener {
            startActivity(Intent(applicationContext(), SignUpActivity::class.java))
        }
        logInButton.setOnClickListener {
            doLogIn()
            (activity as MainActivity).hideKeyboard()
        }
        logOutButton.setOnClickListener {
            doLogOut()
        }
        accountSettingsButton.setOnClickListener {
            startActivity(Intent(applicationContext(), AccountSettingsActivity::class.java))
        }
        profilePic.setOnLongClickListener {
            val builder = AlertDialog.Builder(activity as MainActivity)
            builder.setTitle("Set Your photo")
            builder.setMessage("Do You want to change profile photo?")
            builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                setProfilePhotoFromGallery()
            }
            builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> }
            builder.show()
            return@setOnLongClickListener true
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //updateUI(firebaseAuth.currentUser)
        Log.i("account","account oncreateview")

        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    private fun setProfilePhotoFromGallery() {
        if(applicationContext().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissions, PERMISSION_CODE)
        }else{
            pickImage()
        }
    }

    private fun pickImage() {
        val intent  = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImage()
                }else{
                    Toast.makeText(applicationContext(), "permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode ==  IMAGE_PICK_CODE){
            val uri: Uri = data!!.data!!
            profilePic?.setImageURI(uri)
            uploadProfilePhotoToFirebaseStorage(uri)
        }
    }

    private fun uploadProfilePhotoToFirebaseStorage(uri: Uri) {
        val fileRef = storageReference.child(firebaseAuth.currentUser!!.uid).child("profile.jpg")
        fileRef.putFile(uri).addOnSuccessListener {
            setProfilePhotoFromFirebaseStorage()
            Toast.makeText(applicationContext(), "File uploaded succesfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(applicationContext(), "Upload Failed", Toast.LENGTH_SHORT).show()
        }
    }


    /*override fun onResume() {
        super.onResume()
        if (firebaseAuth.currentUser != null){
            setLookOfAccountFragmentWhenUserIsLoggedIn()
        }
        else{
            setLookOfAccountFragmentWhenUserIsLoggedOut()
        }
    }*/

    private fun doLogOut() {
        firebaseAuth.signOut()
        logInButton.isEnabled = true
        setLookOfAccountFragmentWhenUserIsLoggedOut()
        Toast.makeText(applicationContext(), "You are logged out.", Toast.LENGTH_SHORT).show()
    }

    private fun doLogIn() {
        if(logEmailEditText.text.toString().isEmpty()){
            logEmailEditText.error = "Please enter mail"
            logEmailEditText.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(logEmailEditText.text.toString()).matches()){
            logEmailEditText.error = "Invalid email"
            logEmailEditText.requestFocus()
            return
        }
        if (logPasswordEditText.text.toString().isEmpty()){
            logPasswordEditText.error = "Please enter password"
            logPasswordEditText.requestFocus()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(logEmailEditText.text.toString(), logPasswordEditText.text.toString())
            .addOnCompleteListener(activity as MainActivity) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    (activity as MainActivity).setFragment(1)
                    updateUI(user)
                } else {
                    Toast.makeText(applicationContext(), "Login failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                setLookOfAccountFragmentWhenUserIsLoggedIn()
                // todo to co ma sie wykonać gdy jest się poprawnie zalogowanym
            }
            else{
                Toast.makeText(applicationContext(), "Login failed. Please verify Your mail.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setLookOfAccountFragmentWhenUserIsLoggedOut() {
        accountFragmentTitleTextView?.text = "Account"
        logEmailEditText?.visibility = View.VISIBLE
        logPasswordEditText?.visibility = View. VISIBLE
        logInButton?.visibility = View.VISIBLE
        dontHaveAccountButton?.visibility = View.VISIBLE
        logOutButton?.visibility = View.GONE
        profilePic?.visibility = View.GONE
        lastTrainingTextView?.visibility = View.GONE
        favoriteTypeOfTrainingTextView?.visibility = View.GONE
        accountSettingsButton?.visibility = View.GONE
        personalRecordsTextView?.visibility = View.GONE
        separatorLineImageView?.visibility = View.GONE
        separatorLineImageView2?.visibility = View.GONE
    }
    private fun setLookOfAccountFragmentWhenUserIsLoggedIn(){
        Log.i("account","user is logged in")
        accountFragmentTitleTextView?.text = firebaseAuth.currentUser?.displayName.toString()
        logEmailEditText?.visibility = View.GONE
        logPasswordEditText?.visibility = View. GONE
        logInButton?.visibility = View.GONE
        dontHaveAccountButton?.visibility = View.GONE
        logOutButton?.visibility = View.VISIBLE
        profilePic?.visibility = View.VISIBLE
        lastTrainingTextView?.visibility = View.VISIBLE
        favoriteTypeOfTrainingTextView?.visibility = View.VISIBLE
        accountSettingsButton?.visibility = View.VISIBLE
        personalRecordsTextView?.visibility = View.VISIBLE
        separatorLineImageView?.visibility = View.VISIBLE
        separatorLineImageView2?.visibility = View.VISIBLE
        setWhenWasLastTraining()
        setProfilePhotoFromFirebaseStorage()
    }

    private fun setProfilePhotoFromFirebaseStorage() {

        val profileRef = storageReference.child(firebaseAuth.currentUser!!.uid).child("profile.jpg")
        profileRef.getFile(localFIle).addOnSuccessListener {
            profilePic?.setImageURI(localFIle.toUri())
            Toast.makeText(applicationContext(), "downloading photo complete.", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            profilePic.setImageResource(R.drawable.ic_baseline_account_circle_24)
            Toast.makeText(applicationContext(), "downloading photo failured.", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setWhenWasLastTraining(){
        var lastTraining = ""
        val reference = fDatabase.getReference(firebaseAuth.currentUser!!.uid).child("Last Training Time")
        reference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null) {
                    val time: Long = snapshot.value as Long
                    val diffBetweenNowAndLastTrainingTimeInHours: Int = diffBetweenNowAndLastTrainingTime(time)
                    Log.i("ACC FRAGMENT", "Last training was: $diffBetweenNowAndLastTrainingTimeInHours")
                    lastTraining = diffBetweenNowAndLastTrainingTimeInHours.toString()
                    if ( diffBetweenNowAndLastTrainingTimeInHours >0) {
                        lastTrainingTextView?.text = "The last training session was $lastTraining hours ago"
                    }else{
                        lastTrainingTextView?.text = "The last training session was less than an hour ago"
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

    }

    private fun diffBetweenNowAndLastTrainingTime(time: Long): Int {
        val difference = (System.currentTimeMillis() - time)/1000
        return difference.div(3600).toInt()
    }
}
