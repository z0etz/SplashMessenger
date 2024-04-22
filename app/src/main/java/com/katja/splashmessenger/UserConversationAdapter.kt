package com.katja.splashmessenger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.katja.splashmessenger.databinding.ItemUserNameBinding

interface OnItemClickListener {
    fun onItemClick(userArray: ArrayList<String?>)
}

class UserConversationAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<UserConversationAdapter.ConversationViewHolder>() {

     val conversationList = mutableListOf<Conversation>()
     val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
     val userDao = UserDao()
     val conversationDao = ConversationDao()

    inner class ConversationViewHolder(private val binding: ItemUserNameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val conversation = conversationList[position]
                    val otherUserId = conversation.senderIds.find { it != currentUserId }
                    if (otherUserId != null) {
                        userDao.fetchUserById(otherUserId) { otherUser ->
                            val otherUserName = otherUser?.fullName
                            val userArray = arrayListOf(otherUserName, otherUserId)
                            listener.onItemClick(userArray)
                        }
                    }
                }
            }
        }

        fun bind(conversation: Conversation, position: Int) {
            val otherUserId = conversation.senderIds.find { it != currentUserId }
            if (otherUserId != null) {
                userDao.fetchUserById(otherUserId) { otherUser ->
                    val otherUserName = otherUser?.fullName ?: "Unknown User"
                    binding.textViewUserName.text = otherUserName
                }
            }
            binding.userConversationDeleteButton.setOnClickListener {
                deleteConversation(position)
                conversationDao.deleteConversation(currentUserId, conversation.id)
            }
        }

        private fun deleteConversation(position: Int){
            conversationList.removeAt(position)
            notifyDataSetChanged()
        }

    }

    private fun getItem(position: Int): Conversation {
        return conversationList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserNameBinding.inflate(inflater, parent, false)
        return ConversationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bind(conversationList[position], position)
    }

    override fun getItemCount(): Int {
        return conversationList.size
    }


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
