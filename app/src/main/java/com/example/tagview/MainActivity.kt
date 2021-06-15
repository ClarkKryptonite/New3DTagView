package com.example.tagview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.a3dtagfromios.R
import com.example.tagview.adapter.TextTagAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var tagView: TagView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tagView = findViewById(R.id.tagView)

        tagView.setOnClickListener {
            tagView.start()
        }

        initView()
    }

    private fun initView() {
        val list = arrayListOf<String>().apply {
            for (i in 0 until 50) {
                add("TAG-$i")
            }
        }

        tagView.adapter = TextTagAdapter(list)
    }
}