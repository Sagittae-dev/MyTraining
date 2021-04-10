package com.example.mytraining.newtraining

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytraining.Exercise
import com.example.mytraining.R
import com.example.mytraining.Series
import com.example.mytraining.newtraining.TrainingActivity.Companion.exercisesInNewTrainingList
import com.example.mytraining.newtraining.TrainingActivity.Companion.trainingContext
import com.example.mytraining.newtraining.TrainingActivity.Companion.trainingInstance
import kotlinx.android.synthetic.main.activity_training.*

class ExercisesListInNewTrainingRecyclerViewAdapter(var exercisesList: ArrayList<Exercise>, var clickListener: OnExerciseItemListClickListener, var longClickListener: OnExerciseItemLongClickListener) : RecyclerView.Adapter<ExercisesListInNewTrainingRecyclerViewAdapter.ViewHolder>(), SeriesListInExerciseInNewTrainingRecyclerViewAdapter.OnSerieItemLongClickListener {

    //var seriesInExerciseInNewTraining: RecyclerView = RecyclerView(trainingContext())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exercise_item_in_new_training_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int  = exercisesList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = exercisesList[position]

        holder.initialize(exercisesList[position], clickListener, longClickListener)
        holder.seriesCount.text = "Series count: "+ currentItem.listOfSeriesInExrerciseLength.toString()

        holder.seriesInExerciseInNewTraining?.layoutManager = LinearLayoutManager(trainingContext())
        holder.seriesInExerciseInNewTraining?.adapter = SeriesListInExerciseInNewTrainingRecyclerViewAdapter(currentItem.listOfSeriesInExrercise, this, position)
        holder.seriesInExerciseInNewTraining?.adapter?.notifyDataSetChanged()
    }
    var onItemClick: (Int) -> Unit = {}

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val item : Exercise? = null
        val exerciseName = view.findViewById<TextView>(R.id.exerciseNameInNewTraining)
        val seriesCount = view.findViewById<TextView>(R.id.seriesCountTextView)
        val startTime = view.findViewById<TextView>(R.id.startTimeInNewTrainingTextView)

        var seriesInExerciseInNewTraining = view.findViewById<RecyclerView>(R.id.seriesInExerciseInNewTrainingRecyclerView)


        @SuppressLint("SetTextI18n")
        fun initialize(item:Exercise, action: OnExerciseItemListClickListener, action2: OnExerciseItemLongClickListener){
            exerciseName.text = item.exerciseName
            startTime.text = item.startTime
            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
            itemView.setOnLongClickListener {
                action2.onExerciseLongClick(item, adapterPosition)
            }
        }
    }
    interface OnExerciseItemListClickListener{
        fun onItemClick(item: Exercise, position: Int )
    }
    interface OnExerciseItemLongClickListener {
        fun onExerciseLongClick(item: Exercise, position: Int) : Boolean
    }


    override fun onLongItemClick(exerciseNumber: Int, item: Series, position: Int): Boolean {
        //Toast.makeText(trainingContext(), "długo kliiniete $position $item", Toast.LENGTH_SHORT).show()
        val builder = AlertDialog.Builder(trainingContext())
        builder.setTitle("Remove series")
        builder.setMessage("Do You want to remove this series?")
        builder.setPositiveButton("Yes", { dialogInterface: DialogInterface, i: Int ->
            val exercise = exercisesInNewTrainingList[exerciseNumber]
            exercise.listOfSeriesInExrercise.removeAt(position)
            exercise.listOfSeriesInExrerciseLength = exercise.listOfSeriesInExrercise.size
            notifyDataSetChanged()
            trainingInstance?.exercisesListInNewTrainingRecyclerView?.adapter?.notifyDataSetChanged()
        })
        builder.setNegativeButton("No", { dialogInterface: DialogInterface, i: Int ->
        })
        builder.show()
        //showDeleteWindow(exerciseNumber,position)
        return true
    }
}
