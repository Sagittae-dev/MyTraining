package com.example.mytraining.newtraining

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import android.widget.Toast
import com.example.mytraining.R
import com.example.mytraining.newtraining.NewExerciseActivity.Companion.exerciseContext
import com.example.mytraining.newtraining.NewExerciseActivity.Companion.exerciseInstance
import com.example.mytraining.newtraining.TrainingActivity.Companion.trainingContext

class ChooseExerciseExpandableListAdapter(var context: Context, var header: MutableList<String>, var body: MutableList<MutableList<String>> ) : BaseExpandableListAdapter() {
    override fun getGroup(groupPosition: Int): String {
        return header[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        if(convertView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.exercise_layout_group, null)
        }
        val title = convertView?.findViewById<TextView>(R.id.exercise_group_title)
        title?.text = getGroup(groupPosition)
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return body[groupPosition].size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): String {
        return body[groupPosition][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        if(convertView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.exercise_layout_child, null)
        }
        val title = convertView?.findViewById<TextView>(R.id.exercise_group_title)
        title?.text = getChild(groupPosition,childPosition)
        title?.setOnClickListener {
            addExerciseToListInNewTraining(title.text.toString())
            Toast.makeText(exerciseContext(),title.text.toString(),Toast.LENGTH_SHORT).show()
        }
        return convertView
    }


    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return header.size
    }

    private fun addExerciseToListInNewTraining(title: String) {
        val intent= Intent(trainingContext(), TrainingActivity::class.java)
        intent.putExtra("ExerciseName", title)
        exerciseInstance!!.setResult(Activity.RESULT_OK, Intent().putExtra("title",title))
        exerciseInstance!!.finish()
    }
}