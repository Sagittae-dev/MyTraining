package com.example.mytraining.traininghistory

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytraining.R
import com.example.mytraining.Series
import com.example.mytraining.newtraining.SeriesListInExerciseInNewTrainingRecyclerViewAdapter.Companion.dropLastTwoCharsIfItsZero

class SeriesInExerciseRecyclerViewAdapter(private val seriesInExerciseList: ArrayList<Series>) : RecyclerView.Adapter<SeriesInExerciseRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.series_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = seriesInExerciseList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = seriesInExerciseList[position]
        holder.weightTextView.text = "Weight: "+dropLastTwoCharsIfItsZero(currentItem.weight.toString())
        holder.repsTextView.text = "Reps: "+currentItem.reps.toString()
        holder.volumeTextView.text = "Volume = " + dropLastTwoCharsIfItsZero(currentItem.volume.toString())
        holder.annotationTextView?.text = currentItem.annotationsToSeries

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val weightTextView = view.findViewById<TextView>(R.id.weightTextView)
        val repsTextView = view.findViewById<TextView>(R.id.repsTextView)
        val annotationTextView = view.findViewById<TextView>(R.id.annotationToSeriesTextView)
        val volumeTextView = view.findViewById<TextView>(R.id.volumeInListTextView)
    }
}