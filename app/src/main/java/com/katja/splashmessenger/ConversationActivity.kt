package com.katja.splashmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.katja.splashmessenger.databinding.ActivityConversationBinding

class ConversationActivity : AppCompatActivity() {

    lateinit var binding: ActivityConversationBinding
    lateinit var adapter: MessageAdapter
    private val dao = messageDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get conversationId from the intent the activity was started with
        val conversationId = intent.getStringExtra("conversation_id")

        // Initialize the adapter with an empty list
        adapter = MessageAdapter(emptyList())
        binding.messagesRecyclerView.adapter = adapter

        // Call messageDao to get the conversation and update the adapter when it's fetched
        if (conversationId != null) {
            dao.getConversation(conversationId) { conversation ->
                adapter.messageList = conversation
                adapter.notifyDataSetChanged()
            }
        }
    }
}