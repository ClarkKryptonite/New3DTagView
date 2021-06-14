package com.example.tagview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a3dtagfromios.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tagView = findViewById<TagView>(R.id.tagView)

        tagView.setOnClickListener {
            tagView.start()
        }
    }
}