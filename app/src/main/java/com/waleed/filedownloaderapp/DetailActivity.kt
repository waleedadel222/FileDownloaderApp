package com.waleed.filedownloaderapp

import android.app.NotificationManager
import android.os.Bundle
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

        binding.contentDetailFile.backButton.setOnClickListener {
            finish()
        }

        val name: String = intent.getStringExtra("fileName") ?: "default name"
        val status: String = intent.getStringExtra("status") ?: "default status"

        binding.contentDetailFile.fileNameTextView.setTextColor(this.getColor(R.color.teal_200))
        binding.contentDetailFile.fileNameTextView.text = name

        if (status == "SUCCESS") {
            binding.contentDetailFile.statusTextView.setTextColor(this.getColor(R.color.teal_200))
        } else if (status == "FAILED") {
            binding.contentDetailFile.statusTextView.setTextColor( this.getColor(R.color.red))
        }

        binding.contentDetailFile.statusTextView.text = status

    }

}