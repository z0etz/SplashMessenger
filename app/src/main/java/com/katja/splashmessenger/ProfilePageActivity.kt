package com.katja.splashmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.katja.splashmessenger.databinding.ActivityConversationBinding
import com.katja.splashmessenger.databinding.ActivityProfilePageBinding

class ProfilePageActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfilePageBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        auth = Firebase.auth

        val user = auth.currentUser

        binding = ActivityProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileLogoutBtn.setOnClickListener{
            auth.signOut()
            finish()
        }

        binding.profileUserName.text = user?.displayName ?: "No name available"
        binding.profileUserMail.text = user?.email ?: "No email available"

    }
}