package com.example.mytraining.choosenexercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytraining.R
import com.example.mytraining.newtraining.ChoosenSpecificExerciseItem

class ChooseSpecificExerciseRecyclerViewAdapter(val specificExercisesList: ArrayList<ChoosenSpecificExerciseItem>, val onItemClickListener: OnChoosenExerciseItemClickListener) : RecyclerView.Adapter<ChooseSpecificExerciseRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.choose_specific_exercise_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = specificExercisesList[position]
        holder.initialize(currentItem, onItemClickListener)
    }

    override fun getItemCount(): Int = specificExercisesList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var nameOfChooseExerciseItem = view.findViewById<TextView>(R.id.specificExerciseItemTitleTextView)
        var photoOfChooseExerciseItem = view.findViewById<ImageView>(R.id.specificExerciseImage)
        var description = view.findViewById<TextView>(R.id.specificExerciseDescription)

        fun initialize(item: ChoosenSpecificExerciseItem, action: OnChoosenExerciseItemClickListener) {
            nameOfChooseExerciseItem.text = item.nameOfChooseExerciseItem
            photoOfChooseExerciseItem.setImageResource(item.photoOfChooseExerciseItem)
            description.text = item.description
            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }
    }
    interface OnChoosenExerciseItemClickListener{
        fun onItemClick(item: ChoosenSpecificExerciseItem, position: Int )
    }
}