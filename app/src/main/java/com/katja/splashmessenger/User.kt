package com.katja.splashmessenger

class User(var id: String, val fullName: String, val email: String) {

    override fun toString(): String {
        return "User('id = $id', 'fullName = $fullName', 'email = $email')"
    }
}

