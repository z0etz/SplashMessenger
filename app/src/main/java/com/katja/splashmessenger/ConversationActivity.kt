package com.katja.splashmessenger

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.katja.splashmessenger.databinding.ActivityConversationBinding
import java.util.UUID
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration


class ConversationActivity : AppCompatActivity() {

    lateinit var binding: ActivityConversationBinding
    lateinit var adapter: MessageAdapter
    lateinit var auth: FirebaseAuth
    private val dao = messageDao()
    var recyclerViewVisible = false
    var listViewVisible = false
    private val conversationDao = ConversationDao()
    private lateinit var listenerRegistration: ListenerRegistration
    private var selectedMessageType: MessageType = MessageType.NORMAL_VIEW_TYPE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val user = auth.currentUser
        val senderId = user?.uid
        val user2 = intent.getStringArrayListExtra("userArray")
        val user2Id = user2?.get(1)
        binding.conversationName.text = user2?.get(0)

        // the conversation ID should be currentUserId/otherUserID
        val conversationIdUser1 = "${user?.uid}/${user2Id}"
        val conversationIdUser2 = "${user2Id}/${user?.uid}"

        adapter = MessageAdapter(emptyList())
        binding.messagesRecyclerView.adapter = adapter
        binding.messagesRecyclerView.layoutManager = LinearLayoutManager(this)

        getConversation(conversationIdUser1)

        // here starts the firestore changeListner

        val firestore = FirebaseFirestore.getInstance()
        listenerRegistration = firestore.collection("messages/${conversationIdUser1}")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                for (dc in snapshots?.documentChanges!!) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {

                            // A new message has been added
                            getConversation(conversationIdUser1)

                            val recyclerView = binding.messagesRecyclerView

                            recyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, _ ->
                                if (adapter.itemCount > 0) {
                                    if (adapter.itemCount == 1) {
                                        conversationDao.checkIfConversationExists(
                                            conversationIdUser1,
                                            senderId
                                        ) { conversationExists ->
                                            if (!conversationExists) {

                                                val userIdsList = mutableListOf<String?>()
                                                userIdsList.add(senderId)
                                                userIdsList.add(user2Id)

                                                val conversation1 =
                                                    Conversation(conversationIdUser1, userIdsList)
                                                conversationDao.addConversation(
                                                    conversation1,
                                                    senderId
                                                )
                                                val conversation2 =
                                                    Conversation(conversationIdUser2, userIdsList)
                                                conversationDao.addConversation(
                                                    conversation2,
                                                    user2Id
                                                )

                                            }
                                        }

                                    }

                                    if (bottom < recyclerView.height) {
                                        // The layout has been scrolled up, likely due to keyboard being shown
                                        recyclerView.postDelayed({
                                            recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                                        }, 100)
                                    } else {
                                        // The layout has not been scrolled up, perform immediate scroll
                                        recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                                    }
                                } else {
                                    println("avoided a crash")
                                }
                            }
                        }

                        DocumentChange.Type.MODIFIED -> {
                        }

                        DocumentChange.Type.REMOVED -> {
                        }
                    }
                }
            }

        // Here ends the firestore ChangeListener


        val options = arrayOf("Bottle", "Bubble", "Drop", "Splash", "Message")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, options)
        binding.listView.adapter = adapter

        binding.listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                selectedMessageType = when (position) {
                    0 -> MessageType.MESSAGE_IN_BOTTLE
                    1 -> MessageType.WATERBUBBLE
                    2 -> MessageType.WATERDROP
                    3 -> MessageType.WATERSPLASH
                    4 -> MessageType.NORMAL_VIEW_TYPE
                    else -> MessageType.NORMAL_VIEW_TYPE
                }
                listViewVisible = false
                recyclerViewVisible = true
                Log.d(
                    "ConversationActivity",
                    "RecyclerView visibility after selecting option: $recyclerViewVisible"
                )
                updateViewVisibility()
            }

        binding.themeButton.setOnClickListener {
            onShowOptionsClicked()
        }
        binding.sendButton.setOnClickListener {
            val messageText = binding.messageEditText.text.toString()
            val senderId = user?.uid
            val messageID = UUID.randomUUID().toString()
            val currentDate = System.currentTimeMillis()

            // Update MessageType based on selected layout
            val newMessageSender = Message(
                messageID,
                conversationIdUser1,
                senderId,
                selectedMessageType,
                messageText,
                currentDate
            )
            dao.addMessage(newMessageSender)

            val newMessageReceiver = Message(
                messageID,
                conversationIdUser2,
                senderId,
                selectedMessageType,
                messageText,
                currentDate
            )

            dao.addMessage(newMessageReceiver)
            binding.messageEditText.text.clear()
        }
    }

    fun getConversation(conversationId: String?) {

        if (conversationId != null) {
            dao.getConversation(conversationId) { conversation ->
                if (!conversation.isEmpty()) {
                    val conversationSorted = sortMessages(conversation)
                    adapter.messageList = conversationSorted
                    adapter.notifyDataSetChanged()
                }
//             else{
//                   adapter.messageList = spLocal.loadConversation(conversationId)
//                    adapter.notifyDataSetChanged()
//               }
            }
        }
    }

    fun sortMessages(messages: MutableList<Message>): List<Message> {
        val range = messages.size - 2
        for (i in 0..range) {
            for (j in 0..range) {
                if (messages[j].timestamp > messages[j + 1].timestamp) {
                    val swapMessage: Message = messages[j]
                    messages[j] = messages[j + 1]
                    messages[j + 1] = swapMessage
                }
            }
        }
        return messages
    }

    override fun onDestroy() {
        super.onDestroy()
        listenerRegistration.remove()
    }

    fun onShowOptionsClicked() {
        recyclerViewVisible = false
        listViewVisible = !listViewVisible
        updateViewVisibility()
    }

    private fun updateViewVisibility() {
        binding.listView.visibility = if (listViewVisible) View.VISIBLE else View.GONE
        binding.messagesRecyclerView.visibility =
            if (recyclerViewVisible) View.VISIBLE else View.GONE
    }
}

