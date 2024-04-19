package com.katja.splashmessenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.katja.splashmessenger.databinding.ItemWaterdropBinding
import com.katja.splashmessenger.databinding.ItemWatersplashBinding
import com.katja.splashmessenger.databinding.ItemMessageInBottleBinding
import com.katja.splashmessenger.databinding.ItemWaterbubbleBinding
import android.view.animation.AnimationUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MessageAdapter(internal var messageList: List<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val WATERDROP_VIEW_TYPE = 0
    private val WATERSPLASH_VIEW_TYPE = 1
    private val MESSAGE_IN_BOTTLE_VIEW_TYPE = 2
    private val WATERBUBBLE_VIEW_TYPE = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            WATERDROP_VIEW_TYPE -> {
                val view = inflater.inflate(R.layout.item_waterdrop, parent, false)
                val binding = ItemWaterdropBinding.bind(view)
                WaterdropViewHolder(binding)
            }

            WATERSPLASH_VIEW_TYPE -> {
                val view = inflater.inflate(R.layout.item_watersplash, parent, false)
                val binding = ItemWatersplashBinding.bind(view)
                WatersplashViewHolder(binding)
            }
            MESSAGE_IN_BOTTLE_VIEW_TYPE -> {
                val view = inflater.inflate(R.layout.item_message_in_bottle, parent, false)
                val binding = ItemMessageInBottleBinding.bind(view)
                MessageInBottleViewHolder(binding)
            }
            WATERBUBBLE_VIEW_TYPE -> {
                val view = inflater.inflate(R.layout.item_waterbubble, parent, false)
                val binding = ItemWaterbubbleBinding.bind(view)
                WaterbubbleViewHolder(binding)
            }
            else -> {
                throw IllegalArgumentException("Invalid view type")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        when (holder.itemViewType) {
            WATERDROP_VIEW_TYPE -> {
                (holder as WaterdropViewHolder).bind(message)
            }
            WATERSPLASH_VIEW_TYPE -> {
                (holder as WatersplashViewHolder).bind(message)
            }
            MESSAGE_IN_BOTTLE_VIEW_TYPE -> {
                (holder as MessageInBottleViewHolder).bind(message)
            }
            WATERBUBBLE_VIEW_TYPE -> {
                (holder as WaterbubbleViewHolder).bind(message)
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val messageType = messageList[position].type ?: MessageType.WATERBUBBLE
        return messageType.ordinal
    }

    class WaterdropViewHolder(val binding: ItemWaterdropBinding) : RecyclerView.ViewHolder(binding.root) {
        val user = Firebase.auth.currentUser

        fun bind(message: Message) {

            val animation = AnimationUtils.loadAnimation(itemView.context, R.anim.waterdrop_animation)
            val senderId =  message.senderId
            if (user?.uid == senderId) {

                binding.textMessageSentWaterdrop.text = message.text
            }
            else{
                binding.textMessageReceivedWaterdrop.text = message.text
            }


            binding.imageSentMessageWaterdrop.startAnimation(animation)
            binding.imageReceivedMessageWaterdrop.startAnimation(animation)
        }

        }


    class WatersplashViewHolder(val binding: ItemWatersplashBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            // TODO: Implement binding logic once the view is set up
        }
    }

    class MessageInBottleViewHolder(val binding: ItemMessageInBottleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            // TODO: Implement binding logic once the view is set up
        }
    }

    class WaterbubbleViewHolder(val binding: ItemWaterbubbleBinding) : RecyclerView.ViewHolder(binding.root) {
        val user = Firebase.auth.currentUser


        fun bind(message: Message) {
            val animation = AnimationUtils.loadAnimation(itemView.context, R.anim.waterbubble_animation)

            val senderId =  message.senderId
            if (user?.uid == senderId) {

               // binding.textMessageSentWaterbubble.text = message.text
               // binding.textMessageReceivedWaterbubble.visibility = View.GONE
            }
            else{
                binding.textMessageReceivedWaterbubble.text = message.text
                binding.textMessageSentWaterbubble.visibility = View.GONE

            }
            binding.imageSentMessageWaterbubble.startAnimation(animation)
            binding.imageReceivedMessageWaterbubble.startAnimation(animation)
        }
    }
}