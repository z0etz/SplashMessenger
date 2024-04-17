package com.katja.splashmessenger

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.katja.splashmessenger.databinding.ActivityUserConversationBinding

class UserConversationActivity : AppCompatActivity() {

    //OnItemClickListener
    private lateinit var binding: ActivityUserConversationBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserConversationAdapter
    private lateinit var userList: List<User>
    private lateinit var listView: ListView
    private lateinit var searchEditText: EditText
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var originalList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goUser.setOnClickListener {
            val intent = Intent(this, TestSearchActivity::class.java)
            startActivity(intent)
        }

        firestore = FirebaseFirestore.getInstance()

        listView = findViewById(R.id.searchUserList)
        searchEditText = findViewById(R.id.searchEditText)

        getAllUsers()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    // Om söksträngen är tom, återställ listan till ursprungliga tillståndet
                    showUsers(originalList)
                } else {
                    filterUsers(s.toString())
                }
            }
        })

        recyclerView = binding.recyclerViewUserName

        // Dummy list of users (replace with actual data)
       // val userList = listOf(
        //            User("1", "John Doe", "john@example.com", "password"),
        //            User("2", "Jane Smith", "jane@example.com", "password"),
        //            User("3", "Alice Wonderland", "alice@example.com", "password")
        //            // Add more users as needed
        //        )
       // if(userList.isEmpty()) {
        //            binding.startConversationTextView.visibility = View.VISIBLE
        //            binding.noMessageTextView.visibility = View.VISIBLE
        //            binding.messageImageView.visibility = View.VISIBLE
        //            binding.dropletImageView.visibility = View.VISIBLE
        //            binding.recyclerViewUserName.visibility = View.GONE
        //        } else {
        //            binding.startConversationTextView.visibility = View.GONE
        //            binding.noMessageTextView.visibility = View.GONE
        //            binding.messageImageView.visibility = View.GONE
        //            binding.dropletImageView.visibility = View.GONE
        //            binding.recyclerViewUserName.visibility = View.VISIBLE
        //        }
        //
        //        userAdapter = UserConversationAdapter(userList, this)
        //        recyclerView.adapter = userAdapter
        //        recyclerView.layoutManager = LinearLayoutManager(this)
        //    }
        //
        //    override fun onItemClick(userId: String) {
        //        val intent = Intent(this, ConversationActivity::class.java)
        //        intent.putExtra("id", userId)
        //        startActivity(intent)
           }
    private fun getAllUsers() {
        // Hämta en referens till din användarsamling i Firestore
        val usersCollection = firestore.collection("users")

        // Utför en get-operation på användarsamlingen
        usersCollection.get()
            .addOnSuccessListener { querySnapshot ->
                // Lista för att lagra användare
                val usersList = mutableListOf<String>()

                // Iterera över resultaten och lägg till dem i listan
                for (document in querySnapshot.documents) {
                    val fullName = document.getString("fullName")
                    if (fullName != null) {
                        usersList.add(fullName)
                    }
                }

                // Spara den ursprungliga listan för senare användning
                originalList = usersList

                // Visa användarna i listan
                showUsers(usersList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to fetch users: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showUsers(usersList: List<String>) {
        // Skapa en ArrayAdapter för att visa användarna i listview
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, usersList)

        // Tilldela adapter till listview
        listView.adapter = adapter
    }

    private fun filterUsers(query: String) {
        val filteredList = originalList.filter { it.contains(query, ignoreCase = true) }
        showUsers(filteredList)
    }
}
