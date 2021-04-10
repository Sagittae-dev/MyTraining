package com.example.mytraining.traininghistory

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mytraining.MainActivity
import com.example.mytraining.R
import com.example.mytraining.Training

import com.example.mytraining.dummy.DummyContent.DummyItem
import com.example.mytraining.newtraining.SeriesListInExerciseInNewTrainingRecyclerViewAdapter.Companion.dropLastTwoCharsIfItsZero

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class TrainingsHistoryListRecyclerViewAdapter(var trainings: MutableList<Training>, var clickListener: OnHistoryItemListClickListener) : RecyclerView.Adapter<TrainingsHistoryListRecyclerViewAdapter.ViewHolder>() {
    var sortedList = listOf<Training>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.training_item_in_training_history_list, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sortedList = trainings.sortedWith(Comparator { o1, o2 -> if (o1.timeMillisToSort < o2.timeMillisToSort) 1 else -1 })
        val currentItem = sortedList[position]
        holder.initialize(currentItem, clickListener)
    }


    override fun getItemCount(): Int = trainings.size

    fun getItemFromTrainings( item: Int) : Training{
        return sortedList[item]
    }

    fun deleteItemFromTrainings(item: Int)  {
       trainings.remove(sortedList[item])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeDateTextView = view.findViewById<TextView>(R.id.timeDateTextView)
        val accountNameTextView = view.findViewById<TextView>(R.id.accountNameTextView)
        val firstExercise = view.findViewById<TextView>(R.id.firstExerciseInTrainingTextView)

        @SuppressLint("SetTextI18n")
        fun initialize(item: Training, action: OnHistoryItemListClickListener){
            timeDateTextView.text = item.dateAndTime
            accountNameTextView.text = item.accountNameOrTrainedPerson

            val firstItemFromList = item.exercisesOnTrainingList[0]
            firstExercise.text = firstItemFromList.exerciseName +" | "+ dropLastTwoCharsIfItsZero(firstItemFromList.listOfSeriesInExrercise[0].weight.toString()) +" | "+ firstItemFromList.listOfSeriesInExrercise[0].reps
            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }
    }
    interface OnHistoryItemListClickListener {
        fun onItemClick(item: Training, position: Int )
    }
}