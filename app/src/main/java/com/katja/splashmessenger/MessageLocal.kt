package com.katja.splashmessenger

import android.content.Context
import com.google.gson.Gson
import java.security.MessageDigest
import com.google.gson.reflect.TypeToken

class MessageLocal( private val context: Context) {

    private val PREFS_NAME = "conversation_prefs"

    private val gson = Gson()


    fun saveMessages(messages: List<Message>){

        val conversationJason = gson.toJson(messages)
        val hashValue = getHashValue(conversationJason)

        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val oldHash = sharedPreferences.getString("conversation_hash", null)

        if (hashValue != oldHash){


            val editor = sharedPreferences.edit()
            editor.putString("conversation", conversationJason)
            editor.putString("conversation_hash", hashValue)
            editor.apply()
        }

    }

    fun loadConversation(): List<Message> {

        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("conversation", null)
        val type = object : TypeToken<List<Message>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun getHashValue(json: String): String {

        val bytes = MessageDigest.getInstance("SHA-256").digest(json.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }



    }
}