package com.bobabelga.loadapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bobabelga.R
import com.bobabelga.loadapp.MainActivity.Companion.CHANNEL_ID
import com.bobabelga.loadapp.MainActivity.Companion.NOTIFICATION_ID
import com.bobabelga.loadapp.MainActivity.Companion.REQUEST_CODE


fun NotificationManager.sendNotification(fileName: String, statusTxt:String,applicationContext: Context) {
    val snoozeIntent = Intent(applicationContext, DetailActivity::class.java)
    snoozeIntent.putExtra("fileName",fileName)
    snoozeIntent.putExtra("statusTxt",statusTxt)
    val snoozePendingIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    val builder = NotificationCompat.Builder(
        applicationContext,
        CHANNEL_ID
    ).setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(applicationContext.getString(R.string.notification_description))
        .setAutoCancel(true)
        .addAction(R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_button),
            snoozePendingIntent)
    Log.d(MainActivity.TAG, "Progress Succes Intent "+ fileName + " " + statusTxt)
    notify(NOTIFICATION_ID, builder.build())
}
fun NotificationManager.cancelNotifications() {
    cancelAll()
}