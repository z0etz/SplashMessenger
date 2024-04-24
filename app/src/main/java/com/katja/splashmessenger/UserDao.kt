package com.katja.splashmessenger

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

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

        FirebaseFirestore
            .getInstance()
            .document("users/${user.id}")
            .set(dataToStore)
            .addOnSuccessListener { log -> Log.w(TAG, "User added to firestore with id: ${user.id}" ) }
            .addOnFailureListener { log -> Log.w(TAG, "Failed to add user to firestore")}

    }

    // Function to fetch the list of users from Firestore
    fun fetchUserList(completion: (List<User>) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .get()
            .addOnSuccessListener { result ->
                val userList = mutableListOf<User>()
                for (document in result) {
                    val id = document.getString(ID_KEY) ?: ""
                    val fullName = document.getString(FULL_NAME_KEY) ?: ""
                    val email = document.getString(EMAIL_KEY) ?: ""
                    val user = User(id, fullName, email)
                    userList.add(user)
                }
                completion(userList)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to fetch user list from Firestore", exception)
                completion(emptyList())
            }
    }
    // Function to fetch a specific user by ID
    fun fetchUserById(userId: String, completion: (User?) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val id = documentSnapshot.getString(ID_KEY) ?: ""
                val fullName = documentSnapshot.getString(FULL_NAME_KEY) ?: ""
                val email = documentSnapshot.getString(EMAIL_KEY) ?: ""
                val user = User(id, fullName, email)
               // val user = documentSnapshot.toObject(User::class.java)
                completion(user)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to fetch user from Firestore", exception)
                completion(null)
            }
    }
}