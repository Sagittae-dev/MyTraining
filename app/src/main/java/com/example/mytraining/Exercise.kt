package com.example.mytraining


import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Exercise (
    var numberOfExercise: Int = 0,
    var exerciseName : String = "",
    var startTime: String? = "",
    var stopTime: String? = "",
    var duration: String? = "",
    var listOfSeriesInExrercise : ArrayList<Series> = arrayListOf(),
    var listOfSeriesInExrerciseLength : Int?= 0,
    var exerciseAnnotations : String = "No annotations"
) : Serializable

/*, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        TODO("listOfSeriesInExrercise"),
        parcel.readInt(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(exerciseName)
        parcel.writeString(startTime)
        parcel.writeString(stopTime)
        parcel.writeString(duration)
        listOfSeriesInExrerciseLength?.let { parcel.writeInt(it) }
        parcel.writeString(exerciseAnnotations)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Exercise> {
        override fun createFromParcel(parcel: Parcel): Exercise {
            return Exercise(parcel)
        }

        override fun newArray(size: Int): Array<Exercise?> {
            return arrayOfNulls(size)
        }
    }
}*/
