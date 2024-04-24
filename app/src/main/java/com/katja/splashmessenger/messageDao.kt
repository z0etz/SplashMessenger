package com.katja.splashmessenger

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class messageDao {

    val KEY_ID = "id"
    val KEY_CONVERSATION_ID = "conversation_id"
    val KEY_SENDER_ID = "sender_id"
    val KEY_TYPE = "type"
    val KEY_TEXT = "text"
    val KEY_TIMESTAMP = "timestamp"

    fun addMessage(message: Message) {
        val dataToStore = HashMap<String, Any>()

        dataToStore[KEY_ID] = message.id as Any
        dataToStore[KEY_CONVERSATION_ID] = message.conversationId as Any
        dataToStore[KEY_SENDER_ID] = message.senderId as Any
        dataToStore[KEY_TYPE] = message.type as Any
        dataToStore[KEY_TEXT] = message.text as Any
        dataToStore[KEY_TIMESTAMP] = message.timestamp as Any

        FirebaseFirestore
            .getInstance()
            .document("messages/${message.conversationId}/${message.id}")
            .set(dataToStore)
            .addOnSuccessListener {
                Log.i(
                    "SUCCESS",
                    "Added message to Firestore with id: ${message.id}"
                )
            }
            .addOnFailureListener { Log.i("ERROR", "Failed adding message to Firestore") }
    }

    fun getConversation(conversationId: String, callback: (MutableList<Message>) -> Unit) {
        val messageList: MutableList<Message> = mutableListOf()
        FirebaseFirestore
            .getInstance()
            .collection("messages/$conversationId")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val id = document.getString(KEY_ID)
                    val conversationId = document.getString(KEY_CONVERSATION_ID)
                    val senderId = document.getString(KEY_SENDER_ID)
                    val typeString = document.getString(KEY_TYPE)
                    val type = when (typeString) {
                        "WATERDROP" -> MessageType.WATERDROP
                        "WATERSPLASH" -> MessageType.WATERSPLASH
                        "MESSAGE_IN_BOTTLE" -> MessageType.MESSAGE_IN_BOTTLE
                        "WATERBUBBLE" -> MessageType.WATERBUBBLE
                        "NORMAL_VIEW_TYPE" -> MessageType.NORMAL_VIEW_TYPE
                        else -> MessageType.WATERBUBBLE
                    }
                    val text = document.getString(KEY_TEXT)
                    val timestamp = document.getLong(KEY_TIMESTAMP)
                    val message = Message(id, conversationId, senderId, type, text, timestamp!!)
                    messageList.add(message)
                }
                Log.i("SUCCESS", "Successfully fetched conversation from Firestore")
                callback(messageList)
            }
            .addOnFailureListener {
                Log.e("ERROR", "Failed to fetch conversation")
                callback(mutableListOf())
            }
    }
}