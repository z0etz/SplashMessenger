package com.katja.splashmessenger

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.katja.splashmessenger.databinding.ActivityUserConversationBinding


//        android:onClick="showRecyclerView"
// raderat från xml
class UserConversationActivity : AppCompatActivity(), OnItemClickListener{


    private lateinit var binding: ActivityUserConversationBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserConversationAdapter
    private lateinit var userList: List<User>
    private lateinit var listView: ListView
    private lateinit var searchEditText: EditText
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var originalList: List<String>
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    val userMap = mutableMapOf<String?, String?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        searchEditText = findViewById(R.id.searchEditText)
        getAllUsers()

        autoCompleteTextView = findViewById(R.id.searchEditText)
        adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf())
        autoCompleteTextView.setAdapter(adapter)

        recyclerView = binding.recyclerViewUserName

        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedUser = parent.getItemAtPosition(position) as String
            val userId2: String? = userMap[selectedUser]
            val intent = Intent(this, ConversationActivity::class.java)
            // skicka användarinformationen till nästa aktivitet om det behövs
            //intent.putExtra("id2", 3)
            intent.putExtra("id", userId2)
            println("This is the id")
            println(userId2)
            startActivity(intent)
        }

        getAllUsers()
        // Dummy list of users (replace with actual data)
        val userList = listOf(
                 User("1", "John Doe", "john@example.com", "password"),
                  User("2", "Jane Smith", "jane@example.com", "password"),
                  User("3", "Alice Wonderland", "alice@example.com", "password")
                    // Add more users as needed
        )


        if(userList.isEmpty()) {
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

                userAdapter = UserConversationAdapter( this)
                recyclerView.adapter = userAdapter
                recyclerView.layoutManager = LinearLayoutManager(this)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
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

                override fun onItemClick(userId: String) {
                val intent = Intent(this, ConversationActivity::class.java)
                intent.putExtra("id", userId)
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
                showUsers(usersList)
                adapter.addAll(usersList)
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
}