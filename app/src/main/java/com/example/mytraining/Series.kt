package com.example.mytraining

import java.io.Serializable

data class Series (
    var weight : Double? = 0.0,
    var reps : Int? = 0,
    var volume: Double? = 0.0,
    //var exerciseNumber: Int? = 0,
    var annotationsToSeries: String? = "No Annotations"
) : Serializable
