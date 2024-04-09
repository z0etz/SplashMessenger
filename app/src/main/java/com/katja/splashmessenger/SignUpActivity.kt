package com.katja.splashmessenger
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.katja.splashmessenger.databinding.ActivitySignUpBinding
import java.util.UUID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth


class SignUpActivity  : AppCompatActivity()  {
    private lateinit var binding: ActivitySignUpBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val userDao = UserDao() // Flytta userDao initiering till onCreate

        binding.signUpButton.setOnClickListener{
            registerUser()
            val u = createUser()
            userDao.addUser(u)
        }
        binding.goToLogInTextClick.setOnClickListener {
            // Start SignUpActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }

    private fun registerUser() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            Toast.makeText(this, "sucessfull registration", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener {
                Toast.makeText(this, "failed registration", Toast.LENGTH_SHORT).show()
            }
    }

    private fun createUser(): User {
        val name = binding.fullNameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        val user = User(UUID.randomUUID().toString(), name, email, password)
        return user
    }
}
