package com.katja.splashmessenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

interface OnItemClickListener {
    fun onItemClick(userArray: ArrayList<String?>)
}

class UserConversationAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<UserConversationAdapter.ConversationViewHolder>() {

    val conversationList = mutableListOf<Conversation>()
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    val userDao = UserDao()
    val conversationDao = ConversationDao()

    inner class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUserName: TextView = itemView.findViewById(R.id.textViewUserName)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val conversation = conversationList[position]
                    val otherUserId = conversation.senderIds.find { it != currentUserId }
                    if (otherUserId != null) {
                        userDao.fetchUserById(otherUserId) { otherUser ->

                            val otherUserName = otherUser?.fullName
                            val userArray = arrayListOf<String?>(otherUserName, otherUserId)
                            listener.onItemClick(userArray)

                        }
                    }
                }
            }
        }
    }

        private fun getItem(position: Int): Conversation {
            return conversationList[position]
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user_name, parent, false)
            return ConversationViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
            val conversation = conversationList[position]

            // Find the user that is not the current user
            val otherUserId = conversation.senderIds.find { it != currentUserId }
            if (otherUserId != null) {
                userDao.fetchUserById(otherUserId) { otherUser ->
                    // Once the user data is fetched, update the UI accordingly
                    // Assuming holder.textViewUserName is the TextView where you want to display the user's name
                    holder.textViewUserName.text = otherUser?.fullName ?: "Unknown User"
                }
            }
        }

        override fun getItemCount(): Int {
            return conversationList.size
        }

        // Function to update the conversation list
        fun updateList(newList: List<Conversation>) {
            conversationList.clear()
            conversationList.addAll(newList)
            notifyDataSetChanged()
        }

        fun fetchUserConversations() {
            if (currentUserId != null) {
                conversationDao.fetchConversationsForUser(currentUserId) { conversations ->
                    conversationList.clear()
                    conversationList.addAll(conversations)
                    notifyDataSetChanged()
                    println(conversations)
                }
            }
        }
    }

