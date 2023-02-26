package com.waleed.filedownloaderapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat


private const val NOTIFICATION_ID = 0

@RequiresApi(Build.VERSION_CODES.S)
fun NotificationManager.sendNotification(
    messageBody: String, applicationContext: Context, downloadedFileName: String,
    downloadStatus: String, channelId: String
) {

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.apply {
        putExtra("fileName", downloadedFileName)
        putExtra("status", downloadStatus)
    }

    contentIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
            Intent.FLAG_ACTIVITY_SINGLE_TOP

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        channelId
    )

        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(
            applicationContext
                .getString(R.string.notification_title)
        )
        .setContentText(messageBody)
        .setAutoCancel(true)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            "Check Updates",
            contentPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(NOTIFICATION_ID, builder.build())

}