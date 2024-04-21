package com.katja.splashmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.katja.splashmessenger.databinding.ActivityConversationBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

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

        // Get conversationId from the intent the activity was started with
       // val conversationId = intent.getStringExtra("id")
        val user2 = intent.getStringArrayListExtra("userArray")

        val user2Id = user2?.get(1)
        binding.conversationName.text = user2?.get(0)

        // the conversation ID should be currentUserId/otherUserID
        val conversationIdUser1 = "${user?.uid}/${user2Id}"
        val conversationIdUser2 = "${user2Id}/${user?.uid}"

        // Initialize the adapter with an empty list
        adapter = MessageAdapter(emptyList())
        binding.messagesRecyclerView.adapter = adapter

        // Set layout manager
        binding.messagesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Call messageDao to get the conversation and update the adapter when it's fetched
        getConversation(conversationIdUser1)

        binding.sendButton.setOnClickListener {


            val messageText= binding.messageEditText.text.toString()
            val senderId = user?.uid
            val messageID = UUID.randomUUID().toString()
            val currentDate = System.currentTimeMillis()
            val newMessageSender = Message(messageID,conversationIdUser1,senderId, MessageType.NORMAL_VIEW_TYPE, messageText, currentDate)
            dao.addMessage(newMessageSender)

            val newMessageReceiver = Message(messageID,conversationIdUser2,senderId, MessageType.NORMAL_VIEW_TYPE, messageText, currentDate)
            dao.addMessage(newMessageReceiver)


            getConversation(conversationIdUser1)

            binding.messageEditText.text.clear()

            // the scrolling seems to be working but test it after adding date and sorting messages accordingly
            val recyclerView = binding.messagesRecyclerView

            recyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, _ ->
                if (bottom < recyclerView.height) {
                    // The layout has been scrolled up, likely due to keyboard being shown
                    recyclerView.postDelayed({
                        recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                    }, 100) // Adjust the delay as needed
                } else {
                    // The layout has not been scrolled up, perform immediate scroll
                    recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                }
            }


           // println(senderId)
            //println(conversationIdUser1)

        }
    }

    fun getConversation(conversationId: String?) {

        if (conversationId != null) {
            dao.getConversation(conversationId) { conversation ->


                if(!conversation.isEmpty()) {

                    conversation.sortedBy{  it.timestamp!! }

                    println("From conversation")
                    for( i in conversation){

                        println(i.timestamp)
                    }
                    adapter.messageList = conversation
                    adapter.notifyDataSetChanged()
                }
//                else{
//                    adapter.messageList = spLocal.loadConversation()
//                    adapter.notifyDataSetChanged()
//                }
            }
        }


    }

    // should be moved to a DataManager
    fun sortMessages(conversation: List<Message>): List<Message>{



        return emptyList()
    }
}