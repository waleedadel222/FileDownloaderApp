package com.waleed.filedownloaderapp

import android.app.NotificationManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.waleed.filedownloaderapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancelAll()

        val myIntent = intent.extras

        val name: String = intent.getStringExtra("downloadFile") ?: "default name"
        val status: String = intent.getStringExtra("downloadStatus") ?: "default status"


        Log.d("dataIntent", "myIntent: name =${myIntent?.getString("downloadFile")} ")
        Log.d("dataIntent", "myIntent: status =${myIntent?.getString("downloadStatus")} ")
        Log.d("dataIntent", "normal: name = $name ")
        Log.d("dataIntent", "normal: status =$status  ")



        if (status == "Success") {
            binding.contentDetailFile.statusTextView.setTextColor(Color.parseColor("#00FF00"))
            binding.contentDetailFile.fileNameTextView.setTextColor(Color.parseColor("#00FF00"))
        } else if (status == "Fail") {
            binding.contentDetailFile.statusTextView.setTextColor(Color.parseColor("#FF0000"))
            binding.contentDetailFile.fileNameTextView.setTextColor(Color.parseColor("#FF0000"))

        }
        binding.contentDetailFile.fileNameTextView.text = name

        binding.contentDetailFile.statusTextView.text = status


        binding.contentDetailFile.backButton.setOnClickListener {
            intent = null
            finish()
        }
    }

}