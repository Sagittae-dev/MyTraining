package com.example.mytraining

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.mytraining.account.AccountFragment
import com.example.mytraining.newtraining.ChronometerService.Companion.timerIsEnabled
import com.example.mytraining.newtraining.TrainingActivity
import com.example.mytraining.traininghistory.TrainingHistoryFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :  AppCompatActivity() {
    init {
        instance = this
    }

    companion object {
        private var instance: MainActivity? = null
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
    val fireAuth = FirebaseAuth.getInstance()
    var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        val prefs: SharedPreferences = getSharedPreferences("trainingStatus", MODE_PRIVATE)
        val trainingIsEnable = prefs.getBoolean("trainingIsEnabled", false)
        checkIfUserIsLogIn()
        if (timerIsEnabled  || trainingIsEnable) {
            val intent = Intent(applicationContext, TrainingActivity::class.java)
            startActivity(intent)
        }
        setViewPager()
        setFragment(1)
    }

    private fun checkIfUserIsLogIn() {
        if(fireAuth.currentUser != null){
            userName = fireAuth.currentUser?.displayName.toString()
        }
    }

    // PAGERY, FARGMENTY
    private fun getFragmentPagerAdapter() =
        object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment = when (position) {
                0 -> TrainingHistoryFragment()
                1 -> NewTrainingFragment()
                2 -> AccountFragment()
                else -> {
                    Log.wtf("problem", "problem z pagerem")
                    Fragment()
                }
            }
            override fun getCount(): Int = 3
        }

    private fun setViewPager() {
        viewPager.adapter = getFragmentPagerAdapter()
        bottom_navigation.setOnNavigationItemSelectedListener(getBottomNavigationItemSelectedListener())
        viewPager.addOnPageChangeListener(getOnPageChangeListener())
    }

    fun setFragment(fragment: Int) {
        viewPager.setCurrentItem(fragment, false)
    }

    private fun getBottomNavigationItemSelectedListener()=
        BottomNavigationView.OnNavigationItemSelectedListener { item->
            when (item.itemId) {
                R.id.ic_history -> {
                    viewPager.currentItem = 0
                    return@OnNavigationItemSelectedListener true
                }
                R.id.ic_addNewTraining ->{
                    viewPager.currentItem = 1
                    return@OnNavigationItemSelectedListener true
                }
                R.id.ic_account ->{
                    viewPager.currentItem = 2
                    return@OnNavigationItemSelectedListener true
                }else -> return@OnNavigationItemSelectedListener false
            }
        }

    private fun getOnPageChangeListener() =
        object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                bottom_navigation.menu.getItem(position).isChecked = true
            }
        }

// koniec PAGERY, FRAGMENTY

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }
}