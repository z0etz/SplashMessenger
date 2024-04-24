package com.katja.splashmessenger

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class ConversationDao {

    private val KEY_ID = "id"
    private val KEY_SENDER_IDS = "sender_ids"
    private val TAG = "ConversationDao"


    fun addConversation(conversation: Conversation, userId: String?) {
        val dataToStore = HashMap<String, Any>()
        dataToStore[KEY_ID] = conversation.id as Any
        dataToStore[KEY_SENDER_IDS] = conversation.senderIds as Any
        FirebaseFirestore
            .getInstance()
            .document("conversations/${userId}/${conversation.id}")
            .set(dataToStore)
            .addOnSuccessListener {
                Log.i(
                    "SUCCESS",
                    "Added a new conversation to Firestore with id: ${conversation.id}"
                )
            }
            .addOnFailureListener { Log.i("ERROR", "Failed adding the conversation to Firestore") }

    }

    fun fetchConversationsForUser(userId: String, callback: (List<Conversation>) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("conversations/${userId}/${userId}")
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

    fun checkIfConversationExists(
        conversationId: String?,
        currentUserId: String?,
        callback: (Boolean) -> Unit
    ) {

        FirebaseFirestore.getInstance()
            .document("conversations/${currentUserId}/${conversationId}")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val conversationExists = documentSnapshot.exists()
                callback(conversationExists)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to check conversation existence", exception)
                callback(false)
            }
    }

    fun deleteConversation(userId: String?, conversationId: String?) {
        FirebaseFirestore.getInstance()
            .document("conversations/${userId}/${conversationId}")
            .delete()
            .addOnSuccessListener {
                Log.i("SUCCESS", "Conversation deleted from Firestore with id: $conversationId")
            }

            .addOnFailureListener { e ->
                Log.e(
                    "ERROR",
                    "Failed to delete conversation from Firestore with id: $conversationId",
                    e
                )
            }

        FirebaseFirestore.getInstance()
            .collection("messages/${conversationId}")
            .get()
            .addOnSuccessListener { results ->
                for (doument in results.documents) {
                    doument.reference.delete()
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    "ERROR",
                    "Failed to delete messages in conversation from Firestore with id: $conversationId",
                    e
                )
            }

    }
}

