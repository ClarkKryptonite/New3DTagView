package com.example.tagview.adapter

import android.view.View
import android.view.ViewGroup

/**
 * @author kun
 * @since 2021-Jun-15
 */
class EmptyTagAdapter : TagAdapter<Any, View>(arrayListOf()) {
    override fun createView(parent: ViewGroup, position: Int): View {
        return View(parent.context)
    }
}