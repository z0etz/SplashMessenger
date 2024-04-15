package com.katja.splashmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.katja.splashmessenger.databinding.ActivityConversationBinding

class ConversationActivity : AppCompatActivity() {

    lateinit var binding: ActivityConversationBinding
    lateinit var adapter: MessageAdapter
    lateinit var auth: FirebaseAuth
    private val dao = messageDao()
    private val spLocal = MessageLocal(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val user = auth.currentUser

        binding.userLoggedIn.text = "Logged in as: ${user?.displayName ?: user?.email}"

       binding.signOutButton.setOnClickListener{
           auth.signOut()
           finish()
       }

        // Get conversationId from the intent the activity was started with
        val conversationId = intent.getStringExtra("conversationId")

        // Initialize the adapter with an empty list
        adapter = MessageAdapter(emptyList())
        binding.messagesRecyclerView.adapter = adapter

        // Set layout manager
        binding.messagesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Call messageDao to get the conversation and update the adapter when it's fetched
        if (conversationId != null) {
            dao.getConversation(conversationId) { conversation ->


                if(!conversation.isEmpty()) {

                    adapter.messageList = conversation
                    adapter.notifyDataSetChanged()
                }
                else{
                    adapter.messageList = spLocal.loadConversation()
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}