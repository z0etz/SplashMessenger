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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.katja.splashmessenger.databinding.ItemMessageBinding


class MessageAdapter(internal var messageList: List<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val WATERDROP_VIEW_TYPE = 0
    private val WATERSPLASH_VIEW_TYPE = 1
    private val MESSAGE_IN_BOTTLE_VIEW_TYPE = 2
    private val WATERBUBBLE_VIEW_TYPE = 3
    private val NORMAL_VIEW_TYPE = 4

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
            NORMAL_VIEW_TYPE -> {

                val view = inflater.inflate(R.layout.item_message, parent, false)
                val binding = ItemMessageBinding.bind(view)
                MessageBasicViewHolder(binding)
            }
            else -> {
                throw IllegalArgumentException("Invalid view type")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
       // println(message.senderId)
      //  println(message.conversationId)
        println(message.type)
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
            NORMAL_VIEW_TYPE -> {
                (holder as MessageBasicViewHolder).bind(message)

            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val messageType = messageList[position].type ?: MessageType.NORMAL_VIEW_TYPE
        // I was trying to use the basic design but it did not work
       // println(messageType)
        return messageType.ordinal
    }


    // The messages are  being displayed but the animation does not always work
    // sometimes there are big gaps between the messages.

    class WaterdropViewHolder(val binding: ItemWaterdropBinding) : RecyclerView.ViewHolder(binding.root) {
        val user = Firebase.auth.currentUser

        fun bind(message: Message) {

            //Animation
            val animation = AnimationUtils.loadAnimation(itemView.context, R.anim.waterdrop_animation)
            val senderId =  message.senderId
            if (user?.uid == senderId) {

                binding.textMessageSentWaterdrop.text = message.text
                binding.sentMessageWaterdrop.visibility = View.VISIBLE

                binding.imageSentMessageWaterdrop.startAnimation(animation)

                binding.recivedMessageWaterdrop.visibility = View.GONE
            }
            else{

                binding.textMessageReceivedWaterdrop.text = message.text
                binding.recivedMessageWaterdrop.visibility = View.VISIBLE

                binding.imageReceivedMessageWaterdrop.startAnimation(animation)

                binding.sentMessageWaterdrop.visibility = View.GONE
            }


        }

        }


    class WatersplashViewHolder(val binding: ItemWatersplashBinding) : RecyclerView.ViewHolder(binding.root) {
        val user = Firebase.auth.currentUser

        fun bind(message: Message) {

            val animation = AnimationUtils.loadAnimation(itemView.context, R.anim.watersplash_animation)




            val senderId =  message.senderId
            if (user?.uid == senderId) {

                binding.textMessageSentWatersplash.text = message.text
                binding.sentMessageWatersplash.visibility = View.VISIBLE

                binding.imageSentMessageWatersplash.startAnimation(animation)

                binding.sentMessageWatersplash.visibility = View.GONE

            }else{

                binding.textMessageReceivedWatersplash.text = message.text
                binding.recievedMessageWatersplash.visibility = View.VISIBLE

                binding.imageReceivedMessageWatersplash.startAnimation(animation)

                binding.sentMessageWatersplash.visibility = View.GONE
            }

        }
    }

    class MessageInBottleViewHolder(val binding: ItemMessageInBottleBinding) : RecyclerView.ViewHolder(binding.root) {
        val user = Firebase.auth.currentUser

        fun bind(message: Message) {

            val senderId =  message.senderId
            if (user?.uid == senderId) {

                binding.textMessageSentWaterbottle.text = message.text
                binding.sentMessageBottle.visibility = View.VISIBLE
                binding.recivedMessageBottle.visibility = View.GONE

            }else{

                binding.textMessageReceivedWaterbottle.text = message.text
                binding.textMessageReceivedWaterbottle.visibility = View.VISIBLE
                binding.textMessageSentWaterbottle.visibility = View.GONE




            }

        }
    }

    class WaterbubbleViewHolder(val binding: ItemWaterbubbleBinding) : RecyclerView.ViewHolder(binding.root) {
        val user = Firebase.auth.currentUser


        fun bind(message: Message) {
            val animation = AnimationUtils.loadAnimation(itemView.context, R.anim.waterbubble_animation)

            val senderId =  message.senderId
            if (user?.uid == senderId) {

                binding.textMessageSentWaterbubble.text = message.text
                binding.sentMessageWaterbubble.visibility = View.VISIBLE

                binding.imageSentMessageWaterbubble.startAnimation(animation)

                binding.recievedMessageWaterbubble.visibility = View.GONE
            }else{

                binding.textMessageReceivedWaterbubble.text = message.text
                binding.recievedMessageWaterbubble.visibility = View.VISIBLE

                binding.imageReceivedMessageWaterbubble.startAnimation(animation)

                binding.sentMessageWaterbubble.visibility = View.GONE
            }

        }
    }
    class MessageBasicViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        val user = Firebase.auth.currentUser
        fun bind(message: Message) {
            val senderId = message.senderId

            if (user?.uid == senderId) {
                binding.textMessageSent.text = message.text
                binding.recivedMessageBasic.visibility = View.GONE
                binding.sentMessageBasic.visibility = View.VISIBLE

            }else{
                binding.textMessageReceived.text = message.text
                binding.sentMessageBasic.visibility = View.GONE
                binding.recivedMessageBasic.visibility = View.VISIBLE




            }

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