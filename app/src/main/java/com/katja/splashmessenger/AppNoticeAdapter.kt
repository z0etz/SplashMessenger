package com.katja.splashmessenger
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.katja.splashmessenger.databinding.AppNoticeAdapterBinding

class AppNoticeAdapter(private val dataList: List<NoticeData>) :
    RecyclerView.Adapter<AppNoticeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AppNoticeAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(private val binding: AppNoticeAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NoticeData) {
            binding.senderUserName.text = data.getUsername()
            binding.senderMsg.text = data.getMessage()
        }
    }
}