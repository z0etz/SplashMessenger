package com.katja.splashmessenger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.katja.splashmessenger.databinding.ActivityUserConversationBinding

class UserConversationActivity  : AppCompatActivity() {

    lateinit var binding: ActivityUserConversationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}


//searchEditText