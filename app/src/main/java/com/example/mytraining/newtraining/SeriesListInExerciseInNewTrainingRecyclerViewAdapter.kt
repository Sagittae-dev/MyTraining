package com.example.mytraining.newtraining

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytraining.R
import com.example.mytraining.Series

class SeriesListInExerciseInNewTrainingRecyclerViewAdapter(var temporarySeriesList: ArrayList<Series>, val longClickListener: OnSerieItemLongClickListener, val exerciseNumber: Int) : RecyclerView.Adapter<SeriesListInExerciseInNewTrainingRecyclerViewAdapter.ViewHolder>() {
    companion object{
        fun dropLastTwoCharsIfItsZero(text: String):String {
            if (text.contains(".0")) {
                val newtext = text.dropLast(2)
                return newtext
            }else return text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.series_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = temporarySeriesList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = temporarySeriesList[position]
        holder.initialize(currentItem, longClickListener)
    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val item: Series? = null
        val weight = view.findViewById<TextView>(R.id.weightTextView)
        val reps = view.findViewById<TextView>(R.id.repsTextView)
        val volume = view.findViewById<TextView>(R.id.volumeInListTextView)
        val annotation = view.findViewById<TextView>(R.id.annotationToSeriesTextView)

        @SuppressLint("SetTextI18n")
        fun initialize(item: Series, action: OnSerieItemLongClickListener){
            weight.text = "Weight: "+dropLastTwoCharsIfItsZero(item.weight.toString())
            reps.text = "Reps: "+item.reps.toString()
            annotation.text = item.annotationsToSeries
            volume.text = "Volume = ${dropLastTwoCharsIfItsZero(item.volume.toString())}"
            itemView.setOnLongClickListener{
                action.onLongItemClick(exerciseNumber, item, adapterPosition)
            }
        }
    }

    interface OnSerieItemLongClickListener{
        fun onLongItemClick(exerciseNumber: Int, item: Series, position: Int ) : Boolean
    }
}
