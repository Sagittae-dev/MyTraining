package com.example.mytraining.newtraining

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytraining.*
import com.example.mytraining.newtraining.ChronometerService.Companion.chronometerTime
import com.example.mytraining.newtraining.ChronometerService.Companion.timerIsEnabled
import com.example.mytraining.newtraining.ChronometerService.Companion.timerIsPaused
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_training.*
import kotlinx.android.synthetic.main.exercise_item_in_new_training_list.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

class TrainingActivity : AppCompatActivity(), ExercisesListInNewTrainingRecyclerViewAdapter.OnExerciseItemListClickListener, SeriesListInExerciseInNewTrainingRecyclerViewAdapter.OnSerieItemLongClickListener, ExercisesListInNewTrainingRecyclerViewAdapter.OnExerciseItemLongClickListener {

    private val REQUEST_GET_EXERCISE_NAME: Int = 0
    private val REQUEST_GET_SERIES: Int = 1
    var exercisePositionTMP: Int = 0
    var bodyWeight: Double = 0.0
    private var gson = Gson()
    private var accountName: String = ""
    var someListOfSeriesIsEmpty = false
    var fireAuth = FirebaseAuth.getInstance()
    //var fireDatabase = FirebaseDatabase.getInstance().getReference(fireAuth.currentUser!!.uid)

    @SuppressLint("WeekBasedYear")
    private val dateFormat = SimpleDateFormat("dd MMM, YYYY", Locale.US)
    private val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)

    init {
        trainingInstance = this
    }

    companion object{
        var exercisesInNewTrainingList: ArrayList<Exercise> = arrayListOf()
        var trainingInstance: TrainingActivity? = null
        fun trainingContext() : Context { return trainingInstance!! }
        fun timeOrDateFormatWithZero(monthOrMinute: Int): String {
            return if(monthOrMinute <10){
                "0$monthOrMinute"
            } else
                monthOrMinute.toString()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)
        checkIfTheUserIsLogIn()
        infoAboutStartTrainingTimerTextView.visibility = View.VISIBLE
        loadArrayListFromSharedPreferences()
        setAdapterToExercisesList(exercisesInNewTrainingList)
        setAccountName()
        setBodyWeight()
        setOnClickListeners()
        ChronometerService.trainingActivityIsResumed = true
        startTrainingTimerButton.visibility = View.VISIBLE
        addNewExerciseTextView.visibility = View.GONE
        addNewExerciseImageView.visibility = View.GONE

        val prefs: SharedPreferences = getSharedPreferences("trainingStatus", MODE_PRIVATE)
        val trainingIsEnabled = prefs.getBoolean("trainingIsEnabled", false)

        if(trainingIsEnabled && !timerIsEnabled ) {
            showStartExerciseButtonAndHideStartTrainingAndTextView()
            showAlertWhenTrainingWasUnexceptedInterrupted()
            setOnTimeAndDateLongClickListeners()
            setOnClickListeners()
        }

        if (timerIsEnabled || timerIsPaused){
            showStartExerciseButtonAndHideStartTrainingAndTextView()
            setDateAndTimeFieldsFromPreferences()
            setOnTimeAndDateLongClickListeners()
            setOnClickListeners()
            pauseTrainingTimerButton.isEnabled = true
            stopTrainingTimerButton.isEnabled = true
        }
        if (timerIsPaused)
            pauseTrainingTimerButton.text = "resume"
    }

    private fun showAlertWhenTrainingWasUnexceptedInterrupted() {
        val builder = AlertDialog.Builder(trainingContext())
        builder.setTitle("Training has been interrupted!")
        builder.setMessage("Do You want to continue last training?")
        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            setDateAndTimeFieldsFromPreferences()
            pauseTrainingTimerButton.isEnabled = true
            stopTrainingTimerButton.isEnabled = true
            infoAboutStartTrainingTimerTextView.visibility = View.GONE
            val preferences = getSharedPreferences("chronometerPrefs", MODE_PRIVATE)
            chronometerTime = preferences.getInt("chronometerTime",0)
            startService(Intent(this, ChronometerService::class.java))
            setOnClickListeners()
            setOnTimeAndDateLongClickListeners()
        }
        builder.setNegativeButton("No") { _: DialogInterface, _: Int ->

        }
        builder.show()
        startTrainingTimerButton.visibility = View.GONE
    }

    private fun setDateAndTimeFieldsFromPreferences() {
        val prefs = getSharedPreferences("timeAndDate", MODE_PRIVATE)
        editTextDate.text = prefs.getString("date", "")
        editTextTime.text = prefs.getString("time", "")
    }

    @SuppressLint("SetTextI18n")
    private fun setBodyWeight() {
        //val pref: SharedPreferences = getSharedPreferences("nameAndWeight",MODE_PRIVATE)
        bodyWeight = intent.getDoubleExtra("weight", 0.0)
        yourWeightTextView.text = "Your weight: " + bodyWeight.toString()+ "kg"
    }

    private fun hideKeyboard() {
        val view =  this.currentFocus
        if(view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        //else
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun setAccountName() {
        accountName = fireAuth.currentUser?.displayName.toString()
    }

    private fun setOnClickListeners() {
        addNewExerciseImageView.setOnClickListener {
            startNewExercise()
        }
        addNewExerciseTextView.setOnClickListener {
            startNewExercise()
        }
        trainingActivityConstraintLayout.setOnClickListener {
            hideKeyboard()
        }
        exercisesListInNewTrainingRecyclerView.setOnClickListener {
            hideKeyboard()
        }
        startTrainingTimerButton.setOnClickListener {
            startTrainingTimer()
        }
        pauseTrainingTimerButton.setOnClickListener {
            pauseChronometer()
        }
        stopTrainingTimerButton.setOnClickListener {
            finishTraining()
        }
        addAnnotationToTrainingButton.setOnClickListener {
            showAndHideAnnotationEditText()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showAndHideAnnotationEditText() {
        if (annotationToTrainingEditText.visibility == View.GONE){
            annotationToTrainingEditText.visibility = View.VISIBLE
            addAnnotationToTrainingButton.text = "Hide annotation"
            return
        }
        if(annotationToTrainingEditText.visibility == View.VISIBLE){
            annotationToTrainingEditText.visibility = View.GONE
            addAnnotationToTrainingButton.text = "Add annotation to training"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setOnTimeAndDateLongClickListeners() {
        val now = Calendar.getInstance()
        editTextDate.setOnLongClickListener {
            val datePicker = DatePickerDialog(this, { view, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val date = dateFormat.format(selectedDate.time)
                editTextDate.text = date
                saveTimeAndDateInPreferences(null, date)
            },
            now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
            return@setOnLongClickListener true
        }

        editTextTime.setOnLongClickListener {
            val timePicker = TimePickerDialog(this,  { view, hourOfDay, minute ->
            val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                val time = timeFormat.format(selectedTime.time)
                editTextTime.text = time
                saveTimeAndDateInPreferences(time, null)
            },
            now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false)
            timePicker.show()
            return@setOnLongClickListener true
        }

    }

    fun startTrainingTimer(){
        pauseTrainingTimerButton.isEnabled = true
        stopTrainingTimerButton.isEnabled = true
        addAnnotationToTrainingButton.isEnabled = true
        infoAboutStartTrainingTimerTextView.visibility = View.GONE
        showStartExerciseButtonAndHideStartTrainingAndTextView()
        setTimeAndDate()
        setOnTimeAndDateLongClickListeners()
        startChronometer()
    }

    @SuppressLint("CommitPrefEdits")
    private fun startChronometer() {
        startService(Intent(this, ChronometerService::class.java))
        val prefs: SharedPreferences = getSharedPreferences("trainingStatus", MODE_PRIVATE)
        val prefsEdit = prefs.edit()
        prefsEdit.putBoolean("trainingIsEnabled", true)
        prefsEdit.apply()
    }

    @SuppressLint("SetTextI18n")
    fun pauseChronometer(){
        if (!timerIsPaused) {
            pauseTrainingTimerButton.text = "resume"
            timerIsPaused = true
        }
        else {
            pauseTrainingTimerButton.text = "pause"
            timerIsPaused = false
        }
    }

    @SuppressLint("CommitPrefEdits")
    fun finishTraining(){
        if (exercisesInNewTrainingList.size != 0 ) {
            makeSureAllSeriesListsAreNotEmptyAndSaveOnFirebase()
        }else{
            stopTimerAndClearSharedPreferences()
        }
    }
    @SuppressLint("CommitPrefEdits")
    private fun stopTimerAndClearSharedPreferences(){
        stopService(Intent(this, ChronometerService::class.java))
        timerIsEnabled = false
        val timerPrefs = getSharedPreferences("trainingStatus", MODE_PRIVATE)
        timerPrefs.edit().putBoolean("trainingIsEnabled", false).apply()
        val prefs: SharedPreferences = getSharedPreferences("exerciseList", MODE_PRIVATE)
        prefs.edit()?.clear()?.apply()
        chronometerTime = 0
        exercisesListInNewTrainingRecyclerView.adapter?.notifyDataSetChanged()
        finish()
    }

    private fun makeSureAllSeriesListsAreNotEmptyAndSaveOnFirebase() {
            for (exercise: Exercise in exercisesInNewTrainingList){
                if (exercise.listOfSeriesInExrercise.size == 0) {
                    someListOfSeriesIsEmpty = true
                }
            }
            if (!someListOfSeriesIsEmpty){
                saveTrainingInFirebase().execute()
                stopTimerAndClearSharedPreferences()
            } else{
                showEmptySeriesListAlertWindow()
            }
    }

    private fun showEmptySeriesListAlertWindow() {
        val builder = AlertDialog.Builder(trainingContext())
        builder.setTitle("Are You sure?")
        builder.setMessage("Some of the exercises have empty series list. Do You want to save Your training without this exercises?")
        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            clearEmptyExercisesBeforeSaving()
            if (exercisesInNewTrainingList.size != 0) {
                saveTrainingInFirebase().execute()
            }
            stopTimerAndClearSharedPreferences()
            finish()
        }
        builder.setNegativeButton("No") { _: DialogInterface, _: Int ->
        }
        builder.show()
    }

    private fun clearEmptyExercisesBeforeSaving() {
        val iterator = exercisesInNewTrainingList.iterator()
        while (iterator.hasNext()){
            val item =  iterator.next()
            if (item.listOfSeriesInExrercise.size ==0)
                iterator.remove()
        }
        exercisesListInNewTrainingRecyclerView.adapter?.notifyDataSetChanged()
    }

    @SuppressLint("CommitPrefEdits")
    override fun onDestroy() {
        super.onDestroy()
        /*val prefs: SharedPreferences = getSharedPreferences("appSettings", MODE_PRIVATE)
        prefs.edit().putBoolean("timerIsPaused", timerIsPaused)*/

    }
    private fun showStartExerciseButtonAndHideStartTrainingAndTextView() {
        startTrainingTimerButton.visibility = View.GONE
        infoAboutStartTrainingTimerTextView.visibility = View.GONE
        addNewExerciseTextView.visibility = View.VISIBLE
        addNewExerciseImageView.visibility = View.VISIBLE

        // poniżej jakaś animacja, może sie przydać
        /*if (!timerIsEnabled) {
            ObjectAnimator.ofFloat(
                exercisesItemListInTrainingActivityRecyclerView,
                "translationY",
                1500f
            ).apply {
                duration = 10000
                start()
            }
        }else {
            exercisesItemListInTrainingActivityRecyclerView.animate().translationYBy(1500f)
        }*/
    }


    @SuppressLint("SetTextI18n")
    private fun setTimeAndDate() {
        val todayDate = Calendar.getInstance()
        todayDate.set(Calendar.YEAR, LocalDate.now().year)
        todayDate.set(Calendar.MONTH, LocalDate.now().monthValue -1)
        todayDate.set(Calendar.DAY_OF_MONTH, LocalDate.now().dayOfMonth)
        val date = dateFormat.format(todayDate.time)
        editTextDate.text = date

        val todayTime = Calendar.getInstance()
        todayTime.set(Calendar.HOUR_OF_DAY, LocalTime.now().hour)
        todayTime.set(Calendar.MINUTE, LocalTime.now().minute)
        val time = timeFormat.format(todayTime.time)
        editTextTime.text = time

        saveTimeAndDateInPreferences(time, date)

    }

    @SuppressLint("CommitPrefEdits")
    private fun saveTimeAndDateInPreferences(time: String?, date: String?) {
        val prefs = getSharedPreferences("timeAndDate", MODE_PRIVATE)
        val prefsEdit = prefs.edit()
        if (time != null) {
            prefsEdit.putString("time", time)
        }
        if (date != null) {
            prefsEdit.putString("date", date)
        }
        prefsEdit.apply()

    }

    fun startNewExercise(){
        val intent =Intent(this, NewExerciseActivity::class.java)
        startActivityForResult(intent,REQUEST_GET_EXERCISE_NAME)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_GET_EXERCISE_NAME && resultCode == Activity.RESULT_OK){
            val exerciseTitle = data?.getStringExtra("title")
            Toast.makeText(NewExerciseActivity.exerciseContext(),exerciseTitle, Toast.LENGTH_SHORT).show()
            addExerciseToExercisesList(exerciseTitle.toString())
        }
        else if (requestCode == REQUEST_GET_SERIES && resultCode == Activity.RESULT_OK){
            val weight = data?.getDoubleExtra("weight",0.0)
            val reps = data?.getIntExtra("reps", 0)
            val volume = data?.getDoubleExtra("volume",0.0)
            val annotation = data?.getStringExtra("annotation")
            val seriesResult= Series(weight, reps, volume, annotation)
            addSerieToExercise(exercisePositionTMP, seriesResult)
            stopTrainingTimerButton.text = "Finish and save Your training"
        }
    }


    private fun addExerciseToExercisesList(exerciseTitle: String) {
        val startTime = "Start time: " + timeOrDateFormatWithZero(LocalTime.now().hour) +":"+ timeOrDateFormatWithZero(LocalTime.now().minute)
        val exercise = Exercise(exercisesInNewTrainingList.size,exerciseTitle,startTime,"","", arrayListOf(),0)
        exercisesInNewTrainingList.add(exercise)
        exercisesListInNewTrainingRecyclerView?.adapter?.notifyDataSetChanged()
        saveObjectToArrayList()
    }

    private fun saveObjectToArrayList() {
        val prefs: SharedPreferences = getSharedPreferences("exerciseList", MODE_PRIVATE)
        val prefsEditor = prefs.edit()
        val json = gson.toJson(exercisesInNewTrainingList)
        prefsEditor?.putString("Lista", json)
        prefsEditor?.putInt("duration", chronometerTime)
        prefsEditor?.apply()
    }

    private fun loadArrayListFromSharedPreferences() {
        val prefs: SharedPreferences = getSharedPreferences("exerciseList", MODE_PRIVATE)
        val json = prefs.getString("Lista", "")
        exercisesInNewTrainingList = when {
            json.isNullOrEmpty() -> ArrayList()
            else -> gson.fromJson(json, object : TypeToken<ArrayList<Exercise>>() {}.type)
        }
        println(exercisesInNewTrainingList)
    }

    private fun addSerieToExercise(exerciseNumber: Int, series: Series) {
        val exercise = exercisesInNewTrainingList.get(exerciseNumber)
        exercise.listOfSeriesInExrercise.add(series)
        exercise.listOfSeriesInExrerciseLength = exercise.listOfSeriesInExrercise.size
       // setAdapterToSeriesListInExercise()
        exercisesListInNewTrainingRecyclerView?.adapter?.notifyDataSetChanged()
        seriesInExerciseInNewTrainingRecyclerView?.adapter?.notifyDataSetChanged()
        saveObjectToArrayList()
    }

    override fun onItemClick(item: Exercise, position: Int) { // <- zajebista metoda :D  - dodawanie akcji po kliknięciu na pojedynczy item w liście
        Toast.makeText(this, item.startTime, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, NewSeriesActivity::class.java)
        intent.putExtra("bodyWeight", bodyWeight)
        intent.putExtra("exerciseName", item.exerciseName)
        startActivityForResult(intent, REQUEST_GET_SERIES)
        exercisePositionTMP = position
    }
    private fun setAdapterToExercisesList(list: ArrayList<Exercise>) {
        val adapter = ExercisesListInNewTrainingRecyclerViewAdapter(list, this, this)
        exercisesListInNewTrainingRecyclerView?.layoutManager = LinearLayoutManager(this)
        exercisesListInNewTrainingRecyclerView?.adapter = adapter
        exercisesListInNewTrainingRecyclerView.adapter?.notifyDataSetChanged()
    }


    override fun onLongItemClick(exerciseNumber: Int, item: Series, position: Int): Boolean {

        return true
    }

    override fun onExerciseLongClick(item: Exercise, position: Int): Boolean {
        val builder = AlertDialog.Builder(trainingContext())
        builder.setTitle("Remove exercise")
        builder.setMessage("Do You want to remove this exercise?")
        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            exercisesInNewTrainingList.removeAt(position)
            exercisesListInNewTrainingRecyclerView.adapter?.notifyDataSetChanged()
        }
        builder.setNegativeButton("No") { _: DialogInterface, _: Int ->
        }
        builder.show()
        return true
    }

   //FIREBASE

    @SuppressLint("StaticFieldLeak")
    internal inner class saveTrainingInFirebase : AsyncTask<Void, Void, String>() {
        
        override fun doInBackground(vararg params: Void?): String {
            sendAllTrainingToFirebase()
            return "Finished"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            exercisesInNewTrainingList.clear()
            finish()
            val intent = Intent(trainingContext(), MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(trainingContext(), "Training is saved. Look at Your achievements in Training History.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun sendAllTrainingToFirebase() {
        val dateAndTime = "Date: "+ editTextDate.text.toString()+ " Time: " + editTextTime.text.toString()
        val trainingDuration = trainingTimeTextView.text.toString()
        val annotations = annotationToTrainingEditText.text.toString()
        val training = Training(dateAndTime, accountName, exercisesInNewTrainingList, trainingDuration, System.currentTimeMillis(), annotations)

        val fireRef = FirebaseDatabase.getInstance().getReference(fireAuth.currentUser!!.uid)
        fireRef.child("Training History").child(dateAndTime).setValue(training)
        fireRef.child("Last Training Time").setValue(training.timeMillisToSort)
    }
    private fun checkIfTheUserIsLogIn() {
        if(fireAuth.currentUser == null){
            finish()
        }
    }
}


