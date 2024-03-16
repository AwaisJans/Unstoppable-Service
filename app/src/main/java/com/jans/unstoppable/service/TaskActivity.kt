package com.jans.unstoppable.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jans.unstoppable.service.LongRunningService.Companion.isMyServiceRunning
import com.jans.unstoppable.service.LongRunningService.Companion.layLoad
import com.jans.unstoppable.service.LongRunningService.Companion.percentageTV
import com.jans.unstoppable.service.LongRunningService.Companion.progressBar


class TaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        progressBar = findViewById(R.id.progressBar)
        percentageTV = findViewById(R.id.percentageTextView)
        layLoad = findViewById(R.id.layLoading)

        val btnStartTask:TextView = findViewById(R.id.btnStartTask)


        btnStartTask.setOnClickListener{
            val serviceIntent = Intent(this, LongRunningService::class.java)
            startService(serviceIntent)
            percentageTV?.visibility = View.INVISIBLE
            progressBar?.visibility = View.INVISIBLE
        }


    }

    override fun onResume() {
        super.onResume()

        if(isMyServiceRunning(this)){
            layLoad?.visibility = View.VISIBLE
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,MainActivity::class.java))
    }





}