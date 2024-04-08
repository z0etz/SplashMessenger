package com.katja.splashmessenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(private val messageList: List<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val WATERDROP_VIEW_TYPE = 0
    private val WATERSPLASH_VIEW_TYPE = 1
    private val MESSAGE_IN_BOTTLE_VIEW_TYPE = 2
    private val WATERBUBBLE_VIEW_TYPE = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            WATERDROP_VIEW_TYPE -> WaterdropViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_waterdrop, parent, false))
            WATERSPLASH_VIEW_TYPE -> WatersplashViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_watersplash, parent, false))
            MESSAGE_IN_BOTTLE_VIEW_TYPE -> MessageInBottleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_message_in_bottle, parent, false))
            WATERBUBBLE_VIEW_TYPE -> WaterbubbleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_waterbubble, parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        when (holder.itemViewType) {
            WATERDROP_VIEW_TYPE -> {
                // Bind data and set up WaterdropViewHolder
            }
            WATERSPLASH_VIEW_TYPE -> {
                // Bind data and set up WatersplashViewHolder
            }
            MESSAGE_IN_BOTTLE_VIEW_TYPE -> {
                // Bind data and set up MessageInBottleViewHolder
            }
            WATERBUBBLE_VIEW_TYPE -> {
                // Bind data and set up WaterbubbleViewHolder
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

//    override fun getItemViewType(position: Int): Int {
//        // Return view type based on position or some other criteria
//        // Example:
//        return when {
//            messageList[position].type == MessageType.WATERDROP -> WATERDROP_VIEW_TYPE
//            messageList[position].type == MessageType.WATERSPLASH -> WATERSPLASH_VIEW_TYPE
//            messageList[position].type == MessageType.MESSAGE_IN_BOTTLE -> MESSAGE_IN_BOTTLE_VIEW_TYPE
//            messageList[position].type == MessageType.WATERBUBBLE -> WATERBUBBLE_VIEW_TYPE
//            else -> throw IllegalArgumentException("Invalid message type")
//        }
//    }

    // Define view holders for each message type
    class WaterdropViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views and set up the view holder for waterdrop messages
    }

    class WatersplashViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views and set up the view holder for watersplash messages
    }

    class MessageInBottleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views and set up the view holder for message in a bottle messages
    }

    class WaterbubbleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views and set up the view holder for waterbubble messages
    }
}