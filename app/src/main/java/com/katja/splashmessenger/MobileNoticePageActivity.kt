package com.katja.splashmessenger
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.katja.splashmessenger.databinding.ActivityMobileNoticeBinding



class MobileNoticePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMobileNoticeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for reply button
        binding.replyButton.setOnClickListener {
            // Handle reply button click
            // Navigate to another activity
            val intent = Intent(this, UserConversationActivity::class.java)
            startActivity(intent)
        }
    }


}