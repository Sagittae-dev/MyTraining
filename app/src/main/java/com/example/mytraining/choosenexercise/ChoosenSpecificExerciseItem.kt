package com.example.mytraining.newtraining

import java.io.Serializable

data class ChoosenSpecificExerciseItem(
    var nameOfChooseExerciseItem: String = "",
    var photoOfChooseExerciseItem: Int,
    var description: String = "",
    var type: TypeOfChoosenExercise
) : Serializable

enum class TypeOfChoosenExercise{
    SQUATS,
    DEADLIFTS,
    CHEST,
    BACK,
    BUTTOCKS,
    SHOULDERS,
    ARMS,
    CORE
}