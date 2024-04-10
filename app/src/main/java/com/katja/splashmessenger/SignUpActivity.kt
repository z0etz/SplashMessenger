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
import com.google.firebase.auth.ktx.userProfileChangeRequest
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        userDao = UserDao()

        binding.signUpButton.setOnClickListener {
            registerUser()
        }

        binding.goToLogInTextClick.setOnClickListener {
            // Start LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val confirmPassword = binding.passwordConfirmEditText.text.toString()
        val username = binding.fullNameEditText.text.toString()

        if (password != confirmPassword) {
            // Visa ett felmeddelande om lösenorden inte matchar
            Toast.makeText(this, "Lösenorden matchar inte", Toast.LENGTH_SHORT).show()
            return
        }

        // Registrera användaren med e-post och lösenord om lösenorden matchar
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { authResult ->
            val user = auth.currentUser
            // Ställ in användarnamnet för inloggad användare
            val profileUpdates = userProfileChangeRequest {
                displayName = username
            }
            user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Skapa ett User-objekt med användarnamn och e-post
                    val newUser = createUser(username, email, password)

                    // Lägg till användaren i databasen
                    userDao.addUser(newUser)

                    // Visa ett framgångsmeddelande
                    Toast.makeText(this, "Registrering lyckades", Toast.LENGTH_SHORT).show()
                } else {
                    // Visa ett felmeddelande om uppdateringen av användarnamnet misslyckades
                    Toast.makeText(this, "Misslyckades med att ställa in användarnamn", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener { exception ->
            // Visa ett felmeddelande om registreringen misslyckades
            Toast.makeText(this, "Registrering misslyckades: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun createUser(username: String, email: String, password: String): User {
        val user = User(UUID.randomUUID().toString(), username, email, password)
        return user
    }
}
