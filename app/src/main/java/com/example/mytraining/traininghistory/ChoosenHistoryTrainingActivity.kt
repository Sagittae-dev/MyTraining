package com.example.mytraining.traininghistory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytraining.R
import com.example.mytraining.Training
import kotlinx.android.synthetic.main.activity_choosen_history_training.*
import kotlinx.android.synthetic.main.fragment_training_history_list.*

class ChoosenHistoryTrainingActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choosen_history_training)
        setAllInformation()
    }

    private fun setAllInformation() {
        val trainingInfoFromIntent = intent?.getSerializableExtra("Training") as Training?
        dateAndTimeTextView?.text = trainingInfoFromIntent?.dateAndTime
        accountNameTextView?.text = trainingInfoFromIntent?.accountNameOrTrainedPerson
        annotationsInOneTrainingTextView?.text = trainingInfoFromIntent?.annotations

        val exercisesInOneTrainingList = trainingInfoFromIntent?.exercisesOnTrainingList

        //val adapter = ArrayAdapter(MainActivity.applicationContext(), android.R.layout.simple_list_item_1, trainingInfoFromIntent.exercisesOnTrainingList)

        exercisesInOneTrainingRecyclerView?.layoutManager = LinearLayoutManager(this)
        exercisesInOneTrainingRecyclerView?.adapter = ExercisesInChoosenHistoryTrainingRecyclerViewAdapter(exercisesInOneTrainingList!!)
        listOfHistoryTrainingRecyclerView?.adapter?.notifyDataSetChanged()

    }
}
