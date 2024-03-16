package com.jans.unstoppable.service

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.jans.unstoppable.service.LongRunningService.Companion.isMyServiceRunning

class MainActivity : AppCompatActivity() {


    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    @RequiresApi(33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val task1Btn = findViewById<Button>(R.id.task1)
        val task2Btn = findViewById<Button>(R.id.task2)
        val task3Btn = findViewById<Button>(R.id.task3)
        val task4Btn = findViewById<Button>(R.id.task4)
        val task5Btn = findViewById<Button>(R.id.task5)


        if(isMyServiceRunning(this)){
                Toast.makeText(this, "service is running", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "service is not running", Toast.LENGTH_SHORT).show()
        }

        // Sets up button.
        task1Btn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startActivity(Intent(this,TaskActivity::class.java))
                finish()
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }

    }














}