package com.example.mytraining.newtraining

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mytraining.R
import com.example.mytraining.choosenexercise.ChooseSpecificTypeExerciseActivity

class NewExerciseActivity : AppCompatActivity() {

    var headerExercisesList: MutableList<String> = arrayListOf()
    var bodyExercisesList: MutableList<MutableList<String>> = arrayListOf()


    var deadliftList: MutableList<String> = arrayListOf()
    var chestExercisesList: MutableList<String> = arrayListOf()
    var exercisesOnBackList: MutableList<String> = arrayListOf()
    var shoulderExercisesList: MutableList<String> = arrayListOf()
    var armExercisesList: MutableList<String> = arrayListOf()

    init {
        exerciseInstance = this
    }

    companion object{
        var exerciseInstance: NewExerciseActivity? = null
        fun exerciseContext() : Context { return exerciseInstance!!}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        //setExercisesList()
    }

    fun chooseTypeYourExercise(view: View){
        val intent = Intent(this, ChooseSpecificTypeExerciseActivity::class.java)
        intent.putExtra("exerciseTag", view.tag.toString())
        startActivity(intent)
    }

    private  fun setExercisesList(){
        title = "Choose Exercise"

        deadliftList.add("Classic deadlift")
        deadliftList.add("Sumo deadlift")
        deadliftList.add("Romanian deadlift")
        deadliftList.add("Straight-legged deadlift")
        deadliftList.add("Trap bar deadlift")

        chestExercisesList.add("Bench press")
        chestExercisesList.add("Dumbbell Bench Press")
        chestExercisesList.add("Incline Bench Press")
        chestExercisesList.add("Decline Bench Press")
        chestExercisesList.add("Incline Dumbbell Bench Press")
        chestExercisesList.add("Decline Dumbbell Bench Press")
        chestExercisesList.add("Dumbbell Flys")
        chestExercisesList.add("Incline dumbbell Flys")
        chestExercisesList.add("Decline dumbbell Flys")
        chestExercisesList.add("Chest Dip")
        chestExercisesList.add("Smith Machine Bench Press")

        exercisesOnBackList.add("Wide Grip Pull Up")
        exercisesOnBackList.add("Chin Up")
        exercisesOnBackList.add("Bent Over Row ")
        exercisesOnBackList.add("Bent Over Dumbbell Row")
        exercisesOnBackList.add("One Arm Dumbbell Row")
        exercisesOnBackList.add("Lat Pull Down")
        exercisesOnBackList.add("Reverse Grip Lat Pull Down")
        exercisesOnBackList.add("Good Mornings")
        exercisesOnBackList.add("Barbell Shrug")
        exercisesOnBackList.add("Dumbbell Shrug")

        shoulderExercisesList.add("Military Press - OHP")
        shoulderExercisesList.add("Barbell Press Behind Neck")
        shoulderExercisesList.add("Dumbbell Shoulder Press")
        shoulderExercisesList.add("Arnold Press")
        shoulderExercisesList.add("Dumbbell Lateral Raise")

        armExercisesList.add("Standing Barbell Curl - Biceps")
        armExercisesList.add("Standing Dumbbell Curl - Biceps")
        armExercisesList.add("Cable Tricep Extension")

        headerExercisesList.add("Squats")
        headerExercisesList.add("Deadlifts")
        headerExercisesList.add("Chest")
        headerExercisesList.add("Back")
        headerExercisesList.add("Schoulder")
        headerExercisesList.add("Arms")

       // bodyExercisesList.add(squatsList)
        bodyExercisesList.add(deadliftList)
        bodyExercisesList.add(chestExercisesList)
        bodyExercisesList.add(exercisesOnBackList)
        bodyExercisesList.add(shoulderExercisesList)
        bodyExercisesList.add(armExercisesList)

        //exercisesItemListInTrainingActivityExpandableListView.setAdapter(ChooseExerciseExpandableListAdapter(this,headerExercisesList,bodyExercisesList))
    }
}