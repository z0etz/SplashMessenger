package com.katja.splashmessenger

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Objects

class UserDao {
    val ID_KEY = "id"
    val FULL_NAME_KEY = "fullName"
    val EMAIL_KEY = "email"
    val PASSWORD_KEY = "password"

    fun addUser(user: User) {
        val dataToStore = HashMap<String,Object>()

        dataToStore.put(ID_KEY, user.id as Object)
        dataToStore.put(FULL_NAME_KEY, user.fullName as Object)
        dataToStore.put(EMAIL_KEY, user.fullName as Object)
        dataToStore.put(PASSWORD_KEY, user.password as Object)

        FirebaseFirestore
            .getInstance()
            .document("users/${user.id}")
            .set(dataToStore)
            .addOnSuccessListener { log -> Log.w(TAG, "User added to firestore with id: ${user.id}" ) }
            .addOnFailureListener { log -> Log.w(TAG, "Failed to add user to firestore")}

    }


}