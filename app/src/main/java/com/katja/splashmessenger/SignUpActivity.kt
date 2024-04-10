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
        userDao = UserDao() // Uppdatera userDao-deklarationen

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
        val username = binding.fullNameEditText.text.toString()

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { authResult ->
            val firebaseUser = authResult.user

            // Skapa ett UserProfileChangeRequest-objekt för att ställa in användarnamnet
            val profileUpdates = userProfileChangeRequest {
                displayName = username
            }

            // Uppdatera användarprofilen med det nya användarnamnet
            firebaseUser?.updateProfile(profileUpdates)?.addOnCompleteListener { profileUpdateTask ->
                if (profileUpdateTask.isSuccessful) {
                    // Användarnamnet har uppdaterats framgångsrikt
                    // Fortsätt med att skapa användaren i din databas
                    val user = createUser(username, email, password)
                    userDao.addUser(user)
                    Toast.makeText(this, "Registrering lyckades", Toast.LENGTH_SHORT).show()
                } else {
                    // Misslyckades med att uppdatera användarnamnet
                    Toast.makeText(this, "Misslyckades med att ställa in användarnamn", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener { exception ->
            // Registreringen misslyckades
            Toast.makeText(this, "Registrering misslyckades: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }



    private fun createUser(username: String, email: String, password: String): User {
        val user = User(UUID.randomUUID().toString(), username, email, password)
        return user
    }
}
