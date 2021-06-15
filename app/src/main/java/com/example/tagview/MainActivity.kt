package com.example.tagview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.a3dtagfromios.R
import com.example.tagview.adapter.TextDrawableAdapter
import com.example.tagview.adapter.TextTagAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var mTagCloudView: TagCloudView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTagCloudView = findViewById(R.id.tagView)

        mTagCloudView.setOnClickListener {
            mTagCloudView.start()
        }

        initView()
    }

    private fun initView() {
        val list = arrayListOf<String>().apply {
            for (i in 0 until 50) {
                add("TAG-$i")
            }
        }

        val textTagAdapter = TextTagAdapter(list)

        val testDrawableAdapter = TextDrawableAdapter(list, R.dimen.text_size_18)

        mTagCloudView.adapter = testDrawableAdapter
    }
}