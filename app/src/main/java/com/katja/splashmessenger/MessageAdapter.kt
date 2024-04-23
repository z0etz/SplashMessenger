package com.katja.splashmessenger

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.recyclerview.widget.RecyclerView
import com.katja.splashmessenger.databinding.ItemWaterdropBinding
import com.katja.splashmessenger.databinding.ItemWatersplashBinding
import com.katja.splashmessenger.databinding.ItemMessageInBottleBinding
import com.katja.splashmessenger.databinding.ItemWaterbubbleBinding
import android.view.animation.AnimationUtils
import androidx.viewbinding.ViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.katja.splashmessenger.databinding.ItemMessageBasicBinding
import com.katja.splashmessenger.databinding.ItemMessageBinding
import com.katja.splashmessenger.databinding.ItemMessageTextBinding


class MessageAdapter(internal var messageList: List<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messageType: MessageType = MessageType.NORMAL_VIEW_TYPE

    fun setMessageType(type: MessageType) {
        messageType = type
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            MessageType.MESSAGE_IN_BOTTLE.ordinal -> {
                val view = inflater.inflate(R.layout.item_message_in_bottle, parent, false)
                val binding = ItemMessageInBottleBinding.bind(view)
                MessageViewHolder(binding)
            }
            MessageType.WATERBUBBLE.ordinal -> {
                val view = inflater.inflate(R.layout.item_waterbubble, parent, false)
                val binding = ItemWaterbubbleBinding.bind(view)
                MessageViewHolder(binding)
            }
            MessageType.WATERSPLASH.ordinal -> {
                val view = inflater.inflate(R.layout.item_watersplash, parent, false)
                val binding = ItemWatersplashBinding.bind(view)
                MessageViewHolder(binding)
            }
            MessageType.WATERDROP.ordinal -> {
                val view = inflater.inflate(R.layout.item_waterdrop, parent, false)
                val binding = ItemWaterdropBinding.bind(view)
                MessageViewHolder(binding)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_message, parent, false)
                val binding = ItemMessageBinding.bind(view)
                MessageViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        Log.d("holder:", holder.toString())
        Log.d("message", message.toString())
        Log.d("------","-----")
        message.type?.ordinal?.let { (holder as MessageViewHolder).bind(message, it) }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return messageType.ordinal
    }


    // The messages are  being displayed but the animation does not always work
    // sometimes there are big gaps between the messages.

    class MessageViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        private val user = Firebase.auth.currentUser

        fun bind(message: Message, viewType: Int) {
            val senderId = message.senderId
            Log.d("binding",binding.toString())
            Log.d("message", message.toString())
           // val animation = when (viewType) {
            //                VIEW_TYPE_WATERDROP -> R.anim.waterdrop_animation
            //                VIEW_TYPE_WATERSPLASH -> R.anim.watersplash_animation
            //                VIEW_TYPE_WATERBUBBLE -> R.anim.waterbubble_animation
            //                else -> null
            //            }.let {
            //                if (it != null) {
            //                    AnimationUtils.loadAnimation(itemView.context, it)
            //                }
            //                else{
            //                    null
            //                }
            //            }

           // Log.d("animation:", animation.toString())
            when (binding) {
                is ItemWaterdropBinding -> {
                    if (user?.uid == senderId) {
                        binding.textMessageSentWaterdrop.text = message.text
                        binding.textMessageSentWaterdrop.visibility = View.VISIBLE
                        binding.imageSentMessageWaterdrop.visibility = View.VISIBLE
                        //animation?.let { binding.imageSentMessageWaterdrop.startAnimation(it)
                        //}
                        binding.textMessageReceivedWaterdrop.visibility = View.GONE
                        binding.imageReceivedMessageWaterdrop.visibility = View.GONE
                    } else {
                        binding.textMessageReceivedWaterdrop.text = message.text
                        binding.textMessageReceivedWaterdrop.visibility = View.VISIBLE
                        binding.imageReceivedMessageWaterdrop.visibility = View.VISIBLE
                        //animation?.let {binding.imageReceivedMessageWaterdrop.startAnimation(it)
                       // }
                        binding.textMessageSentWaterdrop.visibility = View.GONE
                        binding.imageSentMessageWaterdrop.visibility = View.GONE
                    }
                }
                is ItemWatersplashBinding -> {
                    if (user?.uid == senderId) {
                        binding.textMessageSentWatersplash.text = message.text
                        binding.textMessageSentWatersplash.visibility = View.VISIBLE
                        binding.imageSentMessageWatersplash.visibility = View.VISIBLE
                       // animation?.let { binding.imageSentMessageWatersplash.startAnimation(it)
                       // }
                        binding.textMessageReceivedWatersplash.visibility = View.GONE
                        binding.imageReceivedMessageWatersplash.visibility = View.GONE
                    } else {
                        binding.textMessageReceivedWatersplash.text = message.text
                        binding.textMessageReceivedWatersplash.visibility = View.VISIBLE
                        binding.imageReceivedMessageWatersplash.visibility = View.VISIBLE
                       // animation?.let {binding.imageReceivedMessageWatersplash.startAnimation(it)
                       // }
                        binding.textMessageSentWatersplash.visibility = View.GONE
                        binding.imageSentMessageWatersplash.visibility = View.GONE
                    }
                }
                is ItemMessageInBottleBinding -> {
                    if (user?.uid == senderId) {
                        binding.textMessageSentBottle.text = message.text
                        binding.textMessageSentBottle.visibility = View.VISIBLE
                        binding.textMessageReceivedBottle.visibility = View.GONE
                    } else {
                        binding.textMessageReceivedBottle.text = message.text
                        binding.textMessageReceivedBottle.visibility = View.VISIBLE
                        binding.textMessageSentBottle.visibility = View.GONE
                    }
                }
                is ItemWaterbubbleBinding -> {
                    if (user?.uid == senderId) {
                        binding.textMessageSentWaterbubble.text = message.text
                        binding.textMessageSentWaterbubble.visibility = View.VISIBLE
                        binding.imageSentMessageWaterbubble.visibility = View.VISIBLE
                        //animation?.let { binding.imageSentMessageWaterbubble.startAnimation(it)
                      //  }
                        binding.textMessageReceivedWaterbubble.visibility = View.GONE
                        binding.imageReceivedMessageWaterbubble.visibility = View.GONE
                    } else {
                        binding.textMessageReceivedWaterbubble.text = message.text
                        binding.textMessageReceivedWaterbubble.visibility = View.VISIBLE
                        binding.imageReceivedMessageWaterbubble.visibility = View.VISIBLE
                        //animation?.let { binding.imageReceivedMessageWaterbubble.startAnimation(it)
                       // }
                        binding.textMessageSentWaterbubble.visibility = View.GONE
                        binding.imageSentMessageWaterbubble.visibility = View.GONE
                    }
                }
                is ItemMessageBasicBinding -> {
                    if (user?.uid == senderId) {
                        binding.textMessageSent.text = message.text
                        binding.textMessageReceived.visibility = View.GONE
                        binding.textMessageSent.visibility = View.VISIBLE
                    } else {
                        binding.textMessageReceived.text = message.text
                        binding.textMessageSent.visibility = View.GONE
                        binding.textMessageReceived.visibility = View.VISIBLE
                    }
                }
            }
        }

        companion object {
            const val VIEW_TYPE_WATERDROP = 1
            const val VIEW_TYPE_WATERSPLASH = 2
            const val VIEW_TYPE_MESSAGE_IN_BOTTLE = 3
            const val VIEW_TYPE_WATERBUBBLE = 4
            const val VIEW_TYPE_MESSAGE_BASIC = 5
        }
    }
}

/*
fun bind(message: Message) {
    val senderId = message.senderId

    if (user?.uid == senderId) {
        binding.textMessageSent.text = message.text
        binding.textMessageReceived.visibility = View.GONE
        binding.root.removeView(binding.textMessageReceived)

        binding.textMessageSent.visibility = View.VISIBLE  // Make sent message visible


    }else{
        binding.textMessageReceived.text = message.text
        binding.textMessageSent.visibility = View.GONE
        binding.root.removeView(binding.textMessageSent)

        binding.textMessageReceived.visibility = View.VISIBLE // Make received message visible


    }

}

 */