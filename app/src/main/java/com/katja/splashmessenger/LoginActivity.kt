package com.katja.splashmessenger

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.katja.splashmessenger.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        // Kontrollera om användaren redan är inloggad
        if (auth.currentUser != null) {
            // Användaren är redan inloggad, omdirigera till UserConversationActivity
            startActivity(Intent(this, UserConversationActivity::class.java))
            finish()
        }

        binding.logInButton.setOnClickListener {
            longinUser()
        }

        binding.goToSignUpButtton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun longinUser() {
        val email = binding.edUsername.text.toString()
        val password = binding.edPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val user = auth.currentUser
                    Toast.makeText(
                        this,
                        "Welcome: ${user?.displayName ?: user?.email}",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, UserConversationActivity::class.java))
                    finish()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Failed to log in: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
        }

    }


    override fun onResume() {
        super.onResume()
        binding.edUsername.text = null
        binding.edPassword.text = null
    }
}
