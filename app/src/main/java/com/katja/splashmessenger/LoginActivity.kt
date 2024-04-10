package com.katja.splashmessenger

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.katja.splashmessenger.databinding.ActivityLoginBinding
import com.katja.splashmessenger.databinding.ActivitySignUpBinding

class LoginActivity  : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.logInButton.setOnClickListener {
            longinUser()
        }

    }

    private fun longinUser() {
        val email = binding.edUsername.text.toString()
        val password = binding.edPassword.text.toString()

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            startActivity(Intent(this, ConversationActivity:: class.java))
        }
            .addOnSuccessListener {
                Toast.makeText(this, "failed to login", Toast.LENGTH_SHORT).show()
            }
    }
}