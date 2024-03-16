package com.jans.unstoppable.service

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService


// LongRunningService.java
class LongRunningService : Service() {
    private var isRunning = false
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
        isRunning = true
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started")

        handler = Handler()

        createNotificationChannel();
        startForeground(NOTIFICATION_ID, createNotification());


        // Start a background task
        handler!!.postDelayed(updateProgressTask, 1000);



        // Return START_STICKY to ensure the service restarts if it's killed by the system
        return START_STICKY
    }



    private val NOTIFICATION_ID = 1
    private val CHANNEL_ID = "progress_notification"
    private val PROGRESS_MAX = 100
    private var handler: Handler? = null
    private var progress = 0


    private val updateProgressTask: Runnable = object : Runnable {
        override fun run() {
            progress += 10
            if (progress <= PROGRESS_MAX) {
                updateNotification(progress)
                handler!!.postDelayed(this, 5000)
            } else {
                stopSelf()
                percentageTV?.visibility = View.GONE
                progressBar?.visibility = View.GONE
                layLoad?.visibility = View.GONE
            }
        }
    }
    private var notificationManager: NotificationManager?=null

    private fun createNotificationChannel() {
        val name: CharSequence = "Progress Notification"
        val description = "Notification with progress bar"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description
        notificationManager = getSystemService(
            NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Downloading...")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setProgress(PROGRESS_MAX, 0, false)
            .build()
    }

    private fun updateNotification(progress: Int) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        percentageTV?.visibility = View.VISIBLE
        progressBar?.visibility = View.VISIBLE
        layLoad?.visibility = View.GONE
        builder.setContentTitle("Downloading...")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setProgress(PROGRESS_MAX, progress, false)
        percentageTV?.text = "$progress %"

        progressBar?.progress = progress
        val notificationManager = getSystemService(
            NotificationManager::class.java
        )
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
       notificationManager?.cancelAll()
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't need to bind to this service, so return null
        return null
    }

    companion object {
        const val CHANNEL_ID = "dummy_channel"
        private const val TAG = "LongRunningService"


        var progressBar: ProgressBar? = null
        var percentageTV: TextView? = null
        var layLoad: LinearLayout? = null



        fun isMyServiceRunning(context: Context): Boolean {
            val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (LongRunningService::class.java.name == service.service.className) {
                    return true
                }
            }
            return false
        }


    }
}