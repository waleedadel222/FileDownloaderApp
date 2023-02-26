package com.waleed.filedownloaderapp

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.waleed.filedownloaderapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private var downloadedFileName = "fileName"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.includeMain.loadingButton.buttonState = ButtonState.Clicked

        binding.includeMain.loadingButton.setOnClickListener {

            when (binding.radioGroup.checkedRadioButtonId) {
                R.id.glide_radio_button -> {
                    downloadedFileName = getString(R.string.glide_label_button)
                    downloadFile(GLIDE_URL)
                }
                R.id.udacity_radio_button -> {
                    downloadedFileName = getString(R.string.udacity_label_button)

                    downloadFile(UDACITY_URL)
                }
                R.id.retrofit_radio_button -> {
                    downloadedFileName = getString(R.string.retrofit_label_button)
                    downloadFile(RETROFIT_URL)
                }
                else -> {
                    binding.includeMain.loadingButton.buttonState = ButtonState.Clicked

                    downloadedFileName = "fileName"

                    Toast.makeText(
                        applicationContext,
                        "select Download File first",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        }


    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    private val receiver = object : BroadcastReceiver() {

        @RequiresApi(Build.VERSION_CODES.S)
        @SuppressLint("Range")
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            binding.includeMain.loadingButton.buttonState = ButtonState.Completed

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

            val query = DownloadManager.Query()
            query.setFilterById(id!!)

            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                if (DownloadManager.STATUS_SUCCESSFUL == status) {
                    sendNotification(getString(R.string.notification_description), "SUCCESS")

                } else {
                    sendNotification("Download is failed", "FAILED")

                }


            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun sendNotification(toastText: String, downloadStatus: String) {

        val toast = Toast.makeText(
            applicationContext,
            toastText,
            Toast.LENGTH_LONG
        )
        toast.show()

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.sendNotification(
            getString(R.string.notification_description),
            applicationContext,
            downloadedFileName,
            downloadStatus,
            getString(R.string.notification_channel_id)
        )
    }

    private fun createChannel(channelId: String, channelName: String) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.enableVibration(true)
        notificationChannel.description = getString(R.string.notification_description)

        val notificationManager = getSystemService(
            NotificationManager::class.java
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun downloadFile(downloadUrl: String) {

        binding.includeMain.loadingButton.buttonState = ButtonState.Loading

        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_title)
        )


        val request =
            DownloadManager.Request(Uri.parse(downloadUrl))
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
        private const val RETROFIT_URL = "https://github.com/square/retrofit/archive/master.zip"
        private const val GLIDE_URL = "https://github.com/bumptech/glide/archive/master.zip"
        private const val UDACITY_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"

    }


}
