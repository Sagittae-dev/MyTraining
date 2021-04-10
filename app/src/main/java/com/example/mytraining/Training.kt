package com.example.mytraining

import java.io.Serializable

class Training (
    val dateAndTime : String = "",
    val accountNameOrTrainedPerson : String = "",
    val exercisesOnTrainingList : ArrayList<Exercise> = arrayListOf(),
    val trainingDuration: String = "",
    val timeMillisToSort : Long = 0,
    val annotations: String? = null
): Serializable

