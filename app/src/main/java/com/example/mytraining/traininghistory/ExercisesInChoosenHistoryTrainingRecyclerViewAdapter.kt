package com.example.mytraining.traininghistory

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytraining.Exercise
import com.example.mytraining.MainActivity.Companion.applicationContext
import com.example.mytraining.R

class ExercisesInChoosenHistoryTrainingRecyclerViewAdapter(private val exercisesInOneTrainingList: ArrayList<Exercise>) : RecyclerView.Adapter<ExercisesInChoosenHistoryTrainingRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exercese_item_in_choosen_history_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int =exercisesInOneTrainingList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = exercisesInOneTrainingList[position]
        holder.item
        holder.exerciseName.text = currentItem.exerciseName
        holder.numberOfSeries.text = "Series count: "+currentItem.listOfSeriesInExrerciseLength.toString()

        holder.seriesInExerciseRecyclerView?.layoutManager = LinearLayoutManager(applicationContext())
        holder.seriesInExerciseRecyclerView?.adapter = SeriesInExerciseRecyclerViewAdapter(currentItem.listOfSeriesInExrercise)
        holder.seriesInExerciseRecyclerView?.adapter?.notifyDataSetChanged()
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val item : Exercise? = null
        val exerciseName = view.findViewById<TextView>(R.id.exerciseNameTextView)
        val numberOfSeries = view.findViewById<TextView>(R.id.numberOfSeriesTextView)
        val seriesInExerciseRecyclerView = view.findViewById<RecyclerView>(R.id.seriesInExerciseRecyclerView)
    }
}