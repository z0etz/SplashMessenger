package com.katja.splashmessenger
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class AppNoticeAdapter (private val dataList: List<NoticeData>) :
    RecyclerView.Adapter<AppNoticeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.app_notice_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.senderUserName.text = data.getUsername()
        holder.senderMessage.text = data.getMessage()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderUserName: TextView = itemView.findViewById(R.id.sender_user_name)
        val senderMessage: TextView = itemView.findViewById(R.id.sender_msg)
    }
}