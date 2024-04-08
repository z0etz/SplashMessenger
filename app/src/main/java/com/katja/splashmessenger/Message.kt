package com.katja.splashmessenger

data class Message(val id: String?, val conversationId: String?, val senderId: String?,
                   val type: String?, val text: String?, val timestamp: Long?)

enum class MessageType {
    WATERDROP,
    WATERSPLASH,
    MESSAGE_IN_BOTTLE,
    WATERBUBBLE
}