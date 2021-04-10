package com.example.mytraining.traininghistory

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytraining.MainActivity
import com.example.mytraining.MainActivity.Companion.applicationContext
import com.example.mytraining.R
import com.example.mytraining.Training
import com.example.mytraining.firebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_training_history_list.*


class TrainingHistoryFragment : Fragment(), TrainingsHistoryListRecyclerViewAdapter.OnHistoryItemListClickListener {

    val firebaseInstance = FirebaseDatabase.getInstance() // todo referencja do konkretnego usera
    var  trainingsHistoryList = mutableListOf<Training>()
    lateinit var adapter: TrainingsHistoryListRecyclerViewAdapter
    val firebaseDatabase = FirebaseDatabase.getInstance()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
            setListAndAdapter()
        if(firebaseAuth.currentUser != null) {
            setLookOfTrainingHistoryFragmentWhenUserIsLoggedIn()
            }else{
            setLookOfTrainingHistoryFragmentWhenUserIsLoggedOut()
        }
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val builder = AlertDialog.Builder(activity as MainActivity)
                builder.setTitle("Remove Training")
                builder.setMessage("Do You want to remove this Training from history?")
                builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                    val itemToRemove = adapter.getItemFromTrainings(viewHolder.adapterPosition)
                    firebaseInstance.getReference(firebaseAuth.currentUser!!.uid).child("Training History").child(itemToRemove.dateAndTime).removeValue()
                    adapter.deleteItemFromTrainings(viewHolder.adapterPosition)
                    Toast.makeText(applicationContext(), "Element is removed", Toast.LENGTH_SHORT).show()
                    adapter.notifyDataSetChanged()
                }
                builder.setNegativeButton("No") { _: DialogInterface, _: Int ->
                    adapter.notifyDataSetChanged()
                }
                builder.show()
            }
        }
        val itemTouch = ItemTouchHelper(itemTouchHelper)
        itemTouch.attachToRecyclerView(listOfHistoryTrainingRecyclerView)
    }

    private fun setListAndAdapter() {
        listOfHistoryTrainingRecyclerView?.layoutManager = LinearLayoutManager(context)
        adapter = TrainingsHistoryListRecyclerViewAdapter(trainingsHistoryList,this)
        Log.i("oncreated", "on activity created in TH fragment is called, list of trainings size: " + trainingsHistoryList.size)
        listOfHistoryTrainingRecyclerView?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun getTrainingHistoryList() : MutableList<Training>{
        return trainingsHistoryList
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_training_history_list, container, false)
    }

    override fun onItemClick(item: Training, position: Int) {
        val intent = Intent(applicationContext(), ChoosenHistoryTrainingActivity::class.java)
        val sortedList = trainingsHistoryList.sortedWith(Comparator { o1, o2 -> if (o1.timeMillisToSort < o2.timeMillisToSort) 1 else -1 })
        intent.putExtra("Training", sortedList[position])
        startActivity(intent)
    }

    private fun reloadTrainingHistoryList(){
        Log.i("TH Fragment", " reloadTrainingHistoryList()")
        clearTrainingHistoryList()
        updateTrainingHistoryList()
        Log.i("TH fragment", "reloadTrainingHistoryList() is called, list of trainings size: " + trainingsHistoryList.size)
    }


    fun clearTrainingHistoryList(){
        Log.i("main activity", "clearTrainingHistoryList()")
        trainingsHistoryList.clear()
        //listOfHistoryTrainingRecyclerView?.adapter?.notifyDataSetChanged()
    }
    @SuppressLint("SetTextI18n")
    private fun setLookOfTrainingHistoryFragmentWhenUserIsLoggedIn() {
        Log.i("TH Fragment", " setLookOfTrainingHistoryFragmentWhenUserIsLoggedIn()")
        titleFragmentTrainingHistoryTextView.text = "Training history"
        //(activity as MainActivity).updateTrainingHistoryList()
        updateButton.visibility = View.VISIBLE
        reloadTrainingHistoryList()
        updateButton.setOnClickListener {
            reloadTrainingHistoryList()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun setLookOfTrainingHistoryFragmentWhenUserIsLoggedOut() {
        Log.i("TH Fragment", " setLookOfTrainingHistoryFragmentWhenUserIsLoggedOut()")
        titleFragmentTrainingHistoryTextView.text = "There You will see Your training history when you log in"
        clearTrainingHistoryList()
        listOfHistoryTrainingRecyclerView.adapter?.notifyDataSetChanged()
        updateButton.visibility = View.GONE
        updateButton.setOnClickListener {
            Toast.makeText(applicationContext(), "You must be logged in to show Your Training History List", Toast.LENGTH_SHORT).show()
        }
    }
    fun updateTrainingHistoryList() {
        val reference = firebaseDatabase.getReference(firebaseAuth.currentUser!!.uid).child("Training History")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var trainingAlreadyExist = false

                for (data: DataSnapshot in snapshot.children) {
                    val training = data.getValue(Training::class.java)!!
                    for (trainingItem: Training in trainingsHistoryList) {
                        if (trainingItem.dateAndTime == training.dateAndTime) {
                            trainingAlreadyExist = true
                        }
                    }
                    if (!trainingAlreadyExist) {
                        trainingsHistoryList.add(training)
                    }
                    trainingAlreadyExist = false
                }
                Log.i("main activity", "list is updated, list size: " + trainingsHistoryList.size)
                listOfHistoryTrainingRecyclerView?.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
}


