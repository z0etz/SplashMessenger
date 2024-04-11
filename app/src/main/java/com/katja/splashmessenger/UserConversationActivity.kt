package com.katja.splashmessenger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.katja.splashmessenger.User
import com.katja.splashmessenger.UserAdapter
import com.katja.splashmessenger.databinding.ActivityUserConversationBinding

class UserConversationActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserConversationBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Dummy list of users (replace with your actual data)
        val userList = listOf(
            User("1", "John Doe", "john@example.com", "password"),
            User("2", "Jane Smith", "jane@example.com", "password"),
            User("3", "Alice Wonderland", "alice@example.com", "password")
            // Add more users as needed
        )

        recyclerView = binding.recyclerViewUserName
        userAdapter = UserAdapter(userList)
        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
