package com.katja.splashmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
