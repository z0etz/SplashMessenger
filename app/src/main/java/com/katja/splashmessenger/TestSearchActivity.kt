package com.katja.splashmessenger
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class TestSearchActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var searchEditText: EditText
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var originalList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_search)

        firestore = FirebaseFirestore.getInstance()

        listView = findViewById(R.id.listView)
        searchEditText = findViewById(R.id.searchEditText)

        // Hämta och visa alla användare från Firestore
        getAllUsers()

        // Lägg till en lyssnare för EditText för att hantera sökning i listan
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
