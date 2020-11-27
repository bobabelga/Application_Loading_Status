package com.bobabelga.loadapp

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bobabelga.R
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private var fileName = ""
    private var status = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val notificationManager =
            ContextCompat.getSystemService(
                this,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.cancelNotifications()

        fileName = intent.getStringExtra("fileName").toString()
        status = intent.getStringExtra("statusTxt").toString()
        fileNameValue.setText(fileName)
        statusValue.setText(status)


        ok.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}
