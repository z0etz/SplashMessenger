package com.katja.splashmessenger

class NoticeData(private val _username: String, private val _message: String) {

    fun getUsername(): String {
        return _username
    }

    fun getMessage(): String {
        return _message
    }
}
