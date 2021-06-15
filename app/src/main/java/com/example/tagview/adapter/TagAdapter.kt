package com.example.tagview.adapter

import android.view.View
import android.view.ViewGroup
import com.example.tagview.TagPoint
import com.example.tagview.pointRotation

/**
 * Tag adapter
 *
 * @param T data类型
 * @param V View具体类型
 * @property data 数据集
 * @constructor Create empty Tag adapter
 * @author kun
 * @since 2021-Jun-15
 */
abstract class TagAdapter<T, V : View>(val data: List<T>) {
    private val tagPositionList = MutableList(data.size) { TagPoint() }

    abstract fun createView(parent: ViewGroup, position: Int): V

    fun bindPosition(index: Int, x: Double, y: Double, z: Double) {
        tagPositionList[index].x = x
        tagPositionList[index].y = y
        tagPositionList[index].z = z
    }

    fun getCount(): Int = data.count()

    fun getLocationByIndex(index: Int): TagPoint = tagPositionList[index]

    fun updatePointLocationByIndex(
        index: Int,
        direction: TagPoint,
        angle: Double,
        setTagLocation: (point: TagPoint) -> Unit
    ) {
        val newPoint = tagPositionList[index].pointRotation(direction, angle)
        tagPositionList[index] = newPoint
        setTagLocation(newPoint)
    }
}