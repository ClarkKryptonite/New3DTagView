package com.example.tagview.adapter

import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView

/**
 * @author kun
 * @since 2021-Jun-15
 */
class TextTagAdapter(data: List<String>) : TagAdapter<String, TextView>(data) {
    override fun createView(parent: ViewGroup, position: Int): TextView {
        val view = TextView(parent.context)
        view.paint.isFakeBoldText = true
        view.includeFontPadding = false
        view.setLineSpacing(0f, 0f)
        view.text = data[position]
        view.gravity = Gravity.CENTER
        view.textSize = 18f
        return view
    }
}