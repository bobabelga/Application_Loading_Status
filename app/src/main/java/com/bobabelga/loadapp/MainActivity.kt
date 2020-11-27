package com.bobabelga.loadapp

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.bobabelga.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var fileName = ""
    private var statusTxt = ""
    private var downloadID: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(
            CHANNEL_ID,
            CHANNEL_ID
        )

        custom_button.setOnClickListener {
            when {
                radioGlide.isChecked -> {
                    URL = "https://github.com/bumptech/glide"
                    fileName = getString(R.string.glide)
                    download()
                }
                radioLoadApp.isChecked -> {
                    fileName = getString(R.string.laodApp)
                    download()
                }
                radioRetrofit.isChecked -> {
                    URL = "https://github.com/square/retrofit"
                    fileName = getString(R.string.retrofit)
                    download()
                }
                else -> Toast.makeText(
                    this, resources.getString(R.string.toast),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID){
                statusTxt = "Succes"
                custom_button.setBtnState(ButtonState.Completed)
                createNotification(fileName,statusTxt, context!!)
                Log.d(TAG, "Progress Succes "+ fileName + " " + statusTxt)
            } else {
                statusTxt = "Failed"
                custom_button.setBtnState(ButtonState.Completed)
                createNotification(fileName,statusTxt, context!!)
                Log.d(TAG, "Progress Failed"+ fileName + " " + statusTxt)
            }
        }
    }

    private fun download() {
        custom_button.setBtnState(ButtonState.Clicked)
        val request =
                DownloadManager.Request(Uri.parse(URL))
                        .setTitle(getString(R.string.app_name))
                        .setDescription(getString(R.string.app_description))
                        .setRequiresCharging(false)
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
                downloadManager.enqueue(request)

    }

    companion object {
        private var URL =
                "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        const val CHANNEL_ID = "channelId"
        val NOTIFICATION_ID = 0
        val REQUEST_CODE = 0
        val TAG = "Progress"
    }

    private fun createChannel(channelId: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Time for breakfast"
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }
    fun createNotification (file : String , status : String , context: Context){
        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendNotification(file,status, context)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}