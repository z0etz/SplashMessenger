package com.katja.splashmessenger

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import com.katja.splashmessenger.databinding.ActivityUserConversationBinding


//        android:onClick="showRecyclerView"
// raderat från xml
class UserConversationActivity : AppCompatActivity(), OnItemClickListener{


    private lateinit var binding: ActivityUserConversationBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserConversationAdapter
    private lateinit var searchEditText: EditText
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var originalList: List<String>
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    lateinit var auth: FirebaseAuth
    val userMap = mutableMapOf<String?, String?>()
    private val conversationDao = ConversationDao()
    private lateinit var listenerRegistration: ListenerRegistration
    private var thereIsNoConversations: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        searchEditText = binding.searchEditText
        getAllUsers()

        autoCompleteTextView = binding.searchEditText
        adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf())
        autoCompleteTextView.setAdapter(adapter)

        recyclerView = binding.recyclerViewUserName

        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedUser = parent.getItemAtPosition(position) as String
            val userId2: String? = userMap[selectedUser]
            val userArray = arrayListOf<String?>(selectedUser, userId2)
            // [userName , userId]
            val intent = Intent(this, ConversationActivity::class.java)

            intent.putExtra("userArray", userArray)
            startActivity(intent)
        }



        getAllUsers()


        auth = Firebase.auth
        val currentUser = auth.currentUser
        val currentUserId = currentUser?.uid

        userAdapter = UserConversationAdapter( this)
        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

       getConversations(currentUserId)


        // needs to be initilized

        if(thereIsNoConversations) {
                    binding.startConversationTextView.visibility = View.VISIBLE
                    binding.noMessageTextView.visibility = View.VISIBLE
                    binding.messageImageView.visibility = View.VISIBLE
                    binding.dropletImageView.visibility = View.VISIBLE
                    binding.recyclerViewUserName.visibility = View.GONE
              } else {
                    binding.startConversationTextView.visibility = View.GONE
                    binding.noMessageTextView.visibility = View.GONE
                    binding.messageImageView.visibility = View.GONE
                    binding.dropletImageView.visibility = View.GONE
                    binding.recyclerViewUserName.visibility = View.VISIBLE
               }


       // on change listener should be added here
        listenerRegistration = firestore.collection("conversations/${currentUserId}/${currentUserId}")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }


                for (dc in snapshots?.documentChanges!!) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {

                            // A new message has been added
                            getConversations(currentUserId)

                        }

                        DocumentChange.Type.MODIFIED -> {
                            // Handle modified documents
                        }
                        DocumentChange.Type.REMOVED -> {
                            // Handle removed documents
                        }
                    }
                }
            }

        // on change listener should be added here



        val bottomNavigationView = binding.bottomNavigation

        // Set the selected item to messages by default
        bottomNavigationView.selectedItemId = R.id.item_1

        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.item_1 -> {
                    // MessagesActivity
                    startActivityIfNeeded(Intent(this, UserConversationActivity::class.java), 0)
                    true
                }
                R.id.item_2 -> {
                    // ProfileActivity
                    startActivityIfNeeded(Intent(this, ProfilePageActivity::class.java), 0)
                    true
                }
                else -> false
            }
        }



    }
    override fun onItemClick(userArray: ArrayList<String?>) {
        val intent = Intent(this, ConversationActivity::class.java)
        intent.putExtra("userArray", userArray)
        startActivity(intent)
    }
    override fun onResume() {
        super.onResume()
        // Rensa sökfältet
        autoCompleteTextView.setText("")
    }

    private fun getAllUsers() {
        val usersCollection = firestore.collection("users")

        usersCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val usersList = mutableListOf<String>()
                for (document in querySnapshot.documents) {
                    val fullName = document.getString("fullName")
                    val user2Id = document.getString("id")
                    fullName?.let { usersList.add(it) }
                    userMap[fullName] = user2Id
                }
                originalList = usersList
                if (adapter.isEmpty) {
                    adapter.addAll(usersList)
                }
                showUsers(usersList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to fetch users: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun showUsers(usersList: List<String>) {
        adapter.clear()
        adapter.addAll(usersList)
        adapter.notifyDataSetChanged()
    }

    private fun filterUsers(query: String) {
        val filteredList = originalList.filter { it.contains(query, ignoreCase = true) }
        showUsers(filteredList)
    }

    //Added the fetch thing here to test

    fun getConversations(userId: String?){
        if (userId != null) {
            conversationDao.fetchConversationsForUser(userId) { conversations ->
                userAdapter.conversationList.clear()
                userAdapter.conversationList.addAll(conversations)
                userAdapter.notifyDataSetChanged()
                thereIsNoConversations = conversations.isEmpty()

            }
        }
    }
}