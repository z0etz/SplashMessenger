package com.katja.splashmessenger
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.katja.splashmessenger.databinding.ActivitySignUpBinding
import java.util.UUID

class SignUpActivity  : AppCompatActivity()  {
    private lateinit var binding: ActivitySignUpBinding // Deklarera binding som lateinit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater) // Inflera bindingen
        setContentView(binding.root) // Använd binding.root för att sätta aktivitetens layout

        val userDao = UserDao() // Flytta userDao initiering till onCreate

        binding.addPersonButton.setOnClickListener{
            val u = createUser()
            userDao.addUser(u)
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
