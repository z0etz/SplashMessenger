package com.katja.splashmessenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

interface OnItemClickListener {
    fun onItemClick(userId: String)
}

class UserConversationAdapter(private val userList: List<User>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<UserConversationAdapter.UserViewHolder>() {

    private val conversations: List<User> = mutableListOf()

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUserName: TextView = itemView.findViewById(R.id.textViewUserName)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val userId = getItem(position).id
                    listener.onItemClick(userId)
                }
            }
        }
    }

    private fun getItem(position: Int): User {
        return conversations[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_name, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textViewUserName.text = currentUser.fullName
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}