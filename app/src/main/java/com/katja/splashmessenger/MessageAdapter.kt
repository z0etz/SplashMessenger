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
import com.katja.splashmessenger.databinding.ItemMessageBinding
import com.katja.splashmessenger.databinding.ItemMessageBasicBinding
import com.katja.splashmessenger.databinding.ItemMessageTextBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


class MessageAdapter(internal var messageList: List<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //private var messageType: MessageType = MessageType.NORMAL_VIEW_TYPE

    fun setMessageType(type: MessageType) {
        //messageType = type
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

            MessageType.NORMAL_VIEW_TYPE.ordinal-> {

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
       // Log.d("holder:", holder.toString())
       // Log.d("message", message.toString())
       // Log.d("------","-----")
        message.type?.ordinal?.let { (holder as MessageViewHolder).bind(message, it) }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
       // println(message.type!!.ordinal)
        return message.type!!.ordinal
    }

    fun getMessageDate(timestamp: Long): String{

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        sdf.timeZone = TimeZone.getDefault()
        val date = Date(timestamp)
        return sdf.format(date)
    }


    // The messages are  being displayed but the animation does not always work
    // sometimes there are big gaps between the messages.

   inner class MessageViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        private val user = Firebase.auth.currentUser

        fun bind(message: Message, viewType: Int) {
            val senderId = message.senderId
           // Log.d("binding",binding.toString())
           // Log.d("message", message.toString())
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

                        binding.textDateTimeSentWaterdrop.text = getMessageDate(message.timestamp)
                        binding.textDateTimeSentWaterdrop.visibility = View.VISIBLE




                        binding.textMessageReceivedWaterdrop.visibility = View.GONE
                        binding.imageReceivedMessageWaterdrop.visibility = View.GONE
                        binding.textDateTimeReceivedWaterdrop.visibility = View.GONE
                    } else {
                        binding.textMessageReceivedWaterdrop.text = message.text
                        binding.textMessageReceivedWaterdrop.visibility = View.VISIBLE
                        binding.imageReceivedMessageWaterdrop.visibility = View.VISIBLE
                        //animation?.let {binding.imageReceivedMessageWaterdrop.startAnimation(it)
                       // }

                        binding.textDateTimeReceivedWaterdrop.text = getMessageDate(message.timestamp)
                        binding.textDateTimeReceivedWaterdrop.visibility = View.VISIBLE



                        binding.textMessageSentWaterdrop.visibility = View.GONE
                        binding.imageSentMessageWaterdrop.visibility = View.GONE
                        binding.textDateTimeSentWaterdrop.visibility = View.GONE
                    }
                }
                is ItemWatersplashBinding -> {
                   // println(binding)
                    if (user?.uid == senderId) {
                        binding.textMessageSentWatersplash.text = message.text
                        binding.textMessageSentWatersplash.visibility = View.VISIBLE
                        binding.imageSentMessageWatersplash.visibility = View.VISIBLE

                        // animation?.let { binding.imageSentMessageWatersplash.startAnimation(it)
                       // }

                        binding.textDateTimeSentWatersplash.text = getMessageDate(message.timestamp)
                        binding.textDateTimeSentWatersplash.visibility = View.VISIBLE




                        binding.textMessageReceivedWatersplash.visibility = View.GONE
                        binding.imageReceivedMessageWatersplash.visibility = View.GONE
                        binding.textDateTimeReceivedWatersplash.visibility = View.GONE
                    } else {

                        binding.textMessageReceivedWatersplash.text = message.text
                        binding.textMessageReceivedWatersplash.visibility = View.VISIBLE
                        binding.imageReceivedMessageWatersplash.visibility = View.VISIBLE
                       // animation?.let {binding.imageReceivedMessageWatersplash.startAnimation(it)
                       // }

                        binding.textDateTimeReceivedWatersplash.text = getMessageDate(message.timestamp)
                        binding.textDateTimeReceivedWatersplash.visibility = View.VISIBLE




                        binding.textMessageSentWatersplash.visibility = View.GONE
                        binding.imageSentMessageWatersplash.visibility = View.GONE
                        binding.textDateTimeSentWatersplash.visibility = View.GONE
                    }
                }
                is ItemMessageInBottleBinding -> {
                  //  println(binding)
                    if (user?.uid == senderId) {
                        binding.textMessageSentWaterbottle.text = message.text
                        binding.textMessageSentWaterbottle.visibility = View.VISIBLE
                        binding.imageSentMessageWaterBottle.visibility = View.VISIBLE

                        binding.textDateTimeSentWaterbottle.text = getMessageDate(message.timestamp)
                        binding.textDateTimeSentWaterbottle.visibility = View.VISIBLE






                        binding.textMessageReceivedWaterbottle.visibility = View.GONE
                        binding.imageReceivedMessageWaterbottle.visibility = View.GONE
                        binding.textDateTimeReceivedWaterbottle.visibility = View.GONE

                    } else {

                        binding.textMessageReceivedWaterbottle.text = message.text
                        binding.textMessageReceivedWaterbottle.visibility = View.VISIBLE
                        binding.imageReceivedMessageWaterbottle.visibility = View.VISIBLE

                        binding.textDateTimeReceivedWaterbottle.text = getMessageDate(message.timestamp)
                        binding.textDateTimeReceivedWaterbottle.visibility = View.VISIBLE



                        binding.textMessageSentWaterbottle.visibility = View.GONE
                        binding.imageSentMessageWaterBottle.visibility = View.GONE
                        binding.textDateTimeSentWaterbottle.visibility = View.GONE

                    }
                }
                is ItemWaterbubbleBinding -> {
                   // println(binding)
                    if (user?.uid == senderId) {
                        binding.textMessageSentWaterbubble.text = message.text
                        binding.textMessageSentWaterbubble.visibility = View.VISIBLE
                        binding.imageSentMessageWaterbubble.visibility = View.VISIBLE

                        binding.textDateTimeSentWaterbubble.text = getMessageDate(message.timestamp)
                        binding.textDateTimeSentWaterbubble.visibility = View.VISIBLE


                        //animation?.let { binding.imageSentMessageWaterbubble.startAnimation(it)
                      //  }
                        binding.textMessageReceivedWaterbubble.visibility = View.GONE
                        binding.imageReceivedMessageWaterbubble.visibility = View.GONE
                        binding.textDateTimeReceivedWaterbubble.visibility = View.GONE
                    } else {
                        binding.textMessageReceivedWaterbubble.text = message.text
                        binding.textMessageReceivedWaterbubble.visibility = View.VISIBLE
                        binding.imageReceivedMessageWaterbubble.visibility = View.VISIBLE
                        //animation?.let { binding.imageReceivedMessageWaterbubble.startAnimation(it)
                       // }


                        binding.textDateTimeReceivedWaterbubble.text = getMessageDate(message.timestamp)
                        binding.textDateTimeReceivedWaterbubble.visibility = View.VISIBLE



                        binding.textDateTimeSentWaterbubble.visibility = View.GONE
                        binding.textMessageSentWaterbubble.visibility = View.GONE
                        binding.imageSentMessageWaterbubble.visibility = View.GONE
                    }
                }
                is ItemMessageBinding -> {

                    // println(binding)
                   if (user?.uid == senderId) {
                       binding.textMessageSent.text = message.text
                       binding.textMessageSent.visibility = View.VISIBLE


                       binding.textDateTimeSent.text =  getMessageDate(message.timestamp)
                       binding.textDateTimeSent.visibility = View.VISIBLE

                       binding.textMessageReceived.visibility = View.GONE
                       binding.textDateTimeReceived.visibility = View.GONE
                    } else {
                        binding.textMessageReceived.text = message.text
                        binding.textMessageReceived.visibility = View.VISIBLE

                        binding.textDateTimeReceived.text = getMessageDate(message.timestamp)
                        binding.textDateTimeReceived.visibility = View.VISIBLE

                        binding.textMessageSent.visibility = View.GONE
                        binding.textDateTimeSent.visibility = View.GONE

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
        const val VIEW_TYPE_MESSAGE_BASIC = 4
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