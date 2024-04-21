package com.katja.splashmessenger

import java.time.LocalDate
import java.time.LocalDateTime

data class Message(val id: String?, val conversationId: String?, val senderId: String?,
                   val type:  MessageType?, val text: String?, val timestamp: Long?)

enum class MessageType {
    WATERDROP,
    WATERSPLASH,
    MESSAGE_IN_BOTTLE,
    WATERBUBBLE,
    NORMAL_VIEW_TYPE
}