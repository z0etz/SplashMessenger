package com.katja.splashmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        binding.profileLogoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.profileUserName.text = user?.displayName ?: "No name available"
        binding.profileUserMail.text = user?.email ?: "No email available"

        binding.profileDeleteAccountBtn.setOnClickListener {
            val user = auth.currentUser
            val confirmDialog = AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setPositiveButton("Delete") { _, _ ->
                    user?.delete()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                                auth.signOut() // Log out the user after deleting the account
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "Failed to delete account: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                .setNegativeButton("Cancel", null)
                .create()

            confirmDialog.show()
        }


    }

}