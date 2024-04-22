package com.katja.splashmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val buttonSignUp = findViewById<Button>(R.id.buttonSignUp)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)

        buttonSignUp.setOnClickListener {
            // Start SignUpActivity
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Set onClickListener for Login button
        buttonLogin.setOnClickListener {
            // Start LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        if (auth.currentUser != null) {
            // Användaren är inloggad, omdirigera till huvudaktiviteten
            val intent = Intent(this, UserConversationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

