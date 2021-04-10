package com.example.mytraining.newtraining

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.IBinder
import android.widget.TextView
import com.example.mytraining.R
import com.example.mytraining.newtraining.TrainingActivity.Companion.timeOrDateFormatWithZero
import com.example.mytraining.newtraining.TrainingActivity.Companion.trainingContext
import java.util.*

class ChronometerService : Service() {
    private var context : Context = trainingContext()
    private lateinit var timerTextView: TextView
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel

    private val ID = 10
    private val title = "My training is enable"
    private val CHANNEL_ID = "com.example.mytraining"
    private val description = "Test description"
    var timer: Timer = Timer()

    companion object{
        var timerIsEnabled :Boolean = false
        var timerIsPaused = false
        var trainingActivityIsResumed = false
        var chronometerTime :Int = 0
    }

    @SuppressLint("SetTextI18n")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        timerIsEnabled = true
        showNotification("time")
        setChronometer()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        timerIsEnabled = false

        //todo do kasowania timer, czas pozostały, data i godzina, przyciski?
    }

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    private fun setChronometer(){

        timer.scheduleAtFixedRate( object: TimerTask() {
            override fun run() {
                if(trainingActivityIsResumed){
                    context = trainingContext()
                    trainingActivityIsResumed = false
                }
                if (!timerIsPaused) {
                    chronometerTime++
                    saveChronometerTimeInPreferences(chronometerTime)
                }
                val h = (chronometerTime /3600)
                val m = (chronometerTime - h*3600)/60
                val s = chronometerTime - h*3600- m*60
                val timeText = "${timeOrDateFormatWithZero(h)}:${timeOrDateFormatWithZero(m)}:${timeOrDateFormatWithZero(s)}"
                showNotification(timeText)
                timerTextView = (context as Activity).findViewById(R.id.trainingTimeTextView)
                timerTextView.text = timeText
            }
        },0,1000)
    }

    private fun saveChronometerTimeInPreferences(time: Int) {
        val chronometerPreferences = getSharedPreferences("chronometerPrefs", MODE_PRIVATE)
        val prefsEdit = chronometerPreferences.edit()
        prefsEdit.putInt("chronometerTime", chronometerTime)
        prefsEdit.apply()
    }


    private fun showNotification(time: String) {

        notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager

        val intent = Intent(context, TrainingActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        notificationChannel = NotificationChannel(CHANNEL_ID, description, NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.lightColor = Color.GREEN
        notificationManager.createNotificationChannel(notificationChannel)
        val notification : Notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(time)
            .setSmallIcon(R.drawable.noteicon)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources,
                R.drawable.dumbel_icon_small_no_background
            ))
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .build()
        startForeground(ID,notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}