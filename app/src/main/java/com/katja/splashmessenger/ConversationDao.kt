package com.katja.splashmessenger

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class ConversationDao {

    private val KEY_ID = "id"
    private val KEY_SENDER_IDS = "sender_ids"
    private val TAG = "ConversationDao"

    fun fetchConversationsForUser(userId: String, callback: (List<Conversation>) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("conversations")
            .whereArrayContains(KEY_SENDER_IDS, userId)
            .get()
            .addOnSuccessListener { result ->
                val conversationList = mutableListOf<Conversation>()
                for (document in result) {
                    val id = document.getString(KEY_ID) ?: ""
                    val senderIds = document.get(KEY_SENDER_IDS) as? List<String> ?: emptyList()
                    val conversation = Conversation(id, senderIds.toMutableList())
                    conversationList.add(conversation)
                }
                callback(conversationList)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to fetch conversations for user from Firestore", exception)
                callback(emptyList())
            }
    }

    // Other conversation-related operations can be added here
}

