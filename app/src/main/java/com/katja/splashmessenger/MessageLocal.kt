package com.katja.splashmessenger

import android.content.Context
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.security.MessageDigest
import kotlin.collections.MutableList

class MessageLocal(private val context: Context) {

    private val PREFS_NAME = "conversation_prefs"
    private val gson = Gson()
    fun saveMessages(messages: MutableList<Message>, conversationId: String?) {
        val spFileName = generateSharedPreferencesFileName(conversationId!!)
        val conversationJason = gson.toJson(messages)
        val hashValue = getHashValue(conversationJason)
        val sharedPreferences = context.getSharedPreferences(spFileName, Context.MODE_PRIVATE)
        val oldHash = sharedPreferences.getString("conversation_hash", null)

        if (hashValue != oldHash) {
            val editor = sharedPreferences.edit()
            editor.putString("conversation", conversationJason)
            editor.putString("conversation_hash", hashValue)
            editor.apply()
        }
    }

    fun loadConversation(conversationId: String?): MutableList<Message> {
        val spFileName = generateSharedPreferencesFileName(conversationId!!)

        val sharedPreferences = context.getSharedPreferences(spFileName, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("conversation", null)
        val type = object : TypeToken<MutableList<Message>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun getHashValue(json: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(json.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun addMessage(message: Message, conversationId: String?) {
        var messageList = loadConversation(conversationId)
        messageList.add(message)
        saveMessages(messageList, conversationId)
    }

    fun deleteMessage(message: Message, conversationId: String?) {
        var messageList = loadConversation(conversationId)
        messageList.remove(message)
        saveMessages(messageList, conversationId)
    }

    fun generateSharedPreferencesFileName(conversationId: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(conversationId.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}