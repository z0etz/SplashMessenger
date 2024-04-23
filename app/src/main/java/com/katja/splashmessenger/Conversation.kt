package com.katja.splashmessenger

class Conversation(val id: String, val senderIds: MutableList<String?>) {

    override fun toString(): String {
        return "Conversation('id = $id')\n ${senderIds}"
    }

}