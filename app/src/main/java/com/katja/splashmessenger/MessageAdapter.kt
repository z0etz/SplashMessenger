package com.katja.splashmessenger

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.katja.splashmessenger.databinding.ItemWaterdropBinding
import com.katja.splashmessenger.databinding.ItemWatersplashBinding
import com.katja.splashmessenger.databinding.ItemMessageInBottleBinding
import com.katja.splashmessenger.databinding.ItemWaterbubbleBinding
import android.view.animation.AnimationUtils
import androidx.viewbinding.ViewBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.katja.splashmessenger.databinding.ItemMessageBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


class MessageAdapter(internal var messageList: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

            MessageType.NORMAL_VIEW_TYPE.ordinal -> {

                val view = inflater.inflate(R.layout.item_message, parent, false)
                val binding = ItemMessageBinding.bind(view)
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
        message.type?.ordinal?.let { (holder as MessageViewHolder).bind(message, it) }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return message.type!!.ordinal
    }

    fun getMessageDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        sdf.timeZone = TimeZone.getDefault()
        val date = Date(timestamp)
        return sdf.format(date)
    }

    inner class MessageViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val user = Firebase.auth.currentUser

        fun bind(message: Message, viewType: Int) {
            val senderId = message.senderId
            Log.d("binding", binding.toString())
            Log.d("message", message.toString())
            val animation = when (viewType) {
                VIEW_TYPE_WATERDROP -> R.anim.waterdrop_animation
                VIEW_TYPE_WATERSPLASH -> R.anim.watersplash_animation
                VIEW_TYPE_WATERBUBBLE -> R.anim.waterbubble_animation
                else -> null
            }.let {
                if (it != null) {
                    AnimationUtils.loadAnimation(itemView.context, it)
                } else {
                    null
                }
            }
            when (binding) {
                is ItemWaterdropBinding -> {
                    if (user?.uid == senderId) {

                        binding.textMessageSentWaterdrop.text = message.text
                        binding.sentMessageWaterdrop.visibility = View.VISIBLE
                        animation?.let {
                            binding.imageSentMessageWaterdrop.startAnimation(it)
                        }
                        binding.textDateTimeSentWaterdrop.text = getMessageDate(message.timestamp)
                        binding.recivedMessageWaterdrop.visibility = View.GONE
                    } else {
                        binding.textMessageReceivedWaterdrop.text = message.text
                        binding.recivedMessageWaterdrop.visibility = View.VISIBLE
                        animation?.let {
                            binding.imageReceivedMessageWaterdrop.startAnimation(it)
                        }
                        binding.textDateTimeReceivedWaterdrop.text =
                            getMessageDate(message.timestamp)
                        binding.sentMessageWaterdrop.visibility = View.GONE
                    }
                }

                is ItemWatersplashBinding -> {
                    if (user?.uid == senderId) {
                        binding.textMessageSentWatersplash.text = message.text
                        binding.sentMessageWatersplash.visibility = View.VISIBLE
                        animation?.let {
                            binding.imageSentMessageWatersplash.startAnimation(it)
                        }
                        binding.textDateTimeSentWatersplash.text = getMessageDate(message.timestamp)
                        binding.recievedMessageWatersplash.visibility = View.GONE
                    } else {
                        binding.textMessageReceivedWatersplash.text = message.text
                        binding.recievedMessageWatersplash.visibility = View.VISIBLE
                        animation?.let {
                            binding.imageReceivedMessageWatersplash.startAnimation(it)
                        }
                        binding.textDateTimeReceivedWatersplash.text =
                            getMessageDate(message.timestamp)
                        binding.sentMessageWatersplash.visibility = View.GONE
                    }
                }

                is ItemMessageInBottleBinding -> {
                    if (user?.uid == senderId) {
                        binding.textMessageSentWaterbottle.text = message.text
                        binding.sentMessageBottle.visibility = View.VISIBLE
                        animation?.let {
                            binding.imageSentMessageWaterBottle.startAnimation(it)
                        }
                        binding.textDateTimeSentWaterbottle.text = getMessageDate(message.timestamp)
                        binding.recivedMessageBottle.visibility = View.GONE
                    } else {
                        binding.textMessageReceivedWaterbottle.text = message.text
                        binding.recivedMessageBottle.visibility = View.VISIBLE
                        animation?.let {
                            binding.imageReceivedMessageWaterbottle.startAnimation(it)
                        }
                        binding.textDateTimeReceivedWaterbottle.text =
                            getMessageDate(message.timestamp)
                        binding.sentMessageBottle.visibility = View.GONE
                    }
                }

                is ItemWaterbubbleBinding -> {
                    if (user?.uid == senderId) {
                        binding.textMessageSentWaterbubble.text = message.text
                        binding.sentMessageWaterbubble.visibility = View.VISIBLE
                        animation?.let {
                            binding.imageSentMessageWaterbubble.startAnimation(it)
                        }
                        binding.textDateTimeSentWaterbubble.text = getMessageDate(message.timestamp)
                        binding.recievedMessageWaterbubble.visibility = View.GONE
                    } else {
                        binding.textMessageReceivedWaterbubble.text = message.text
                        binding.recievedMessageWaterbubble.visibility = View.VISIBLE
                        animation?.let {
                            binding.imageReceivedMessageWaterbubble.startAnimation(it)
                        }
                        binding.textDateTimeReceivedWaterbubble.text =
                            getMessageDate(message.timestamp)
                        binding.sentMessageWaterbubble.visibility = View.GONE
                    }
                }

                is ItemMessageBinding -> {
                    if (user?.uid == senderId) {
                        binding.textMessageSent.text = message.text
                        binding.sentMessageBasic.visibility = View.VISIBLE
                        binding.textDateTimeSent.text = getMessageDate(message.timestamp)
                        binding.recivedMessageBasic.visibility = View.GONE
                    } else {
                        binding.textMessageReceived.text = message.text
                        binding.recivedMessageBasic.visibility = View.VISIBLE
                        binding.textDateTimeReceived.text = getMessageDate(message.timestamp)
                        binding.sentMessageBasic.visibility = View.GONE

                    }
                }
            }
        }
    }

    companion object {
        const val VIEW_TYPE_WATERDROP = 0
        const val VIEW_TYPE_WATERSPLASH = 1
        const val VIEW_TYPE_MESSAGE_IN_BOTTLE = 2
        const val VIEW_TYPE_WATERBUBBLE = 3
    }
}
