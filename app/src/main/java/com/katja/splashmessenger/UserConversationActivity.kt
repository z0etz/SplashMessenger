package com.katja.splashmessenger

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.katja.splashmessenger.databinding.ActivityUserConversationBinding

class UserConversationActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var binding: ActivityUserConversationBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserConversationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerViewUserName

        // Dummy list of users (replace with actual data)
        val userList = listOf(
            User("1", "John Doe", "john@example.com", "password"),
            User("2", "Jane Smith", "jane@example.com", "password"),
            User("3", "Alice Wonderland", "alice@example.com", "password")
            // Add more users as needed
        )

        if(userList.isEmpty()) {
            binding.startConversationTextView.visibility = View.VISIBLE
            binding.noMessageTextView.visibility = View.VISIBLE
            binding.messageImageView.visibility = View.VISIBLE
            binding.dropletImageView.visibility = View.VISIBLE
            binding.recyclerViewUserName.visibility = View.GONE
        } else {
            binding.startConversationTextView.visibility = View.GONE
            binding.noMessageTextView.visibility = View.GONE
            binding.messageImageView.visibility = View.GONE
            binding.dropletImageView.visibility = View.GONE
            binding.recyclerViewUserName.visibility = View.VISIBLE
        }

        userAdapter = UserConversationAdapter(userList, this)
        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onItemClick(conversationId: String) {
        val intent = Intent(this, ConversationActivity::class.java)
        intent.putExtra("conversationId", conversationId)
        startActivity(intent)
    }
}
