package com.katja.splashmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.katja.splashmessenger.databinding.ActivityAppNoticeBinding

class AppNotice : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AppNoticeAdapter
    private val dataList: MutableList<NoticeData> = mutableListOf()
    private lateinit var binding: ActivityAppNoticeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        recyclerView = binding.rcNotice
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Prepare dummy data
        dataList.add(NoticeData("John Doe", "john@example.com"))
        dataList.add(NoticeData("Jane Doe", "jane@example.com"))

        // Initialize adapter and set it to RecyclerView
        adapter = AppNoticeAdapter(dataList)
        recyclerView.adapter = adapter
    }
}