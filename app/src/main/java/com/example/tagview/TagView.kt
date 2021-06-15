package com.example.tagview

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.example.tagview.adapter.EmptyTagAdapter
import com.example.tagview.adapter.TagAdapter
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * @author kun
 * @since 2021-06-14
 **/
class TagView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ViewGroup(context, attrs, defStyleAttr), Runnable {

    private var outSizePoint = Point()
    private var directionPoint = TagPoint()
    private var angle = 0.002

    private var animateFlag = false

    private val tagViewList = arrayListOf<View>()

    var adapter: TagAdapter<*, *> = EmptyTagAdapter()
        set(value) {
            if (field != value) {
                field = value
                initTagView()
                initTagViewPosition()
                requestLayout()
            }
        }

    init {
        // 获取屏幕宽高
        initScreenInfo()
        // 设置属性
        isClickable = true
        // 随机设置旋转方向
        directionPoint.x = ((0 until 10).random() - 5).toDouble()
        directionPoint.y = ((0 until 10).random() - 5).toDouble()
    }

    private fun initScreenInfo() {
        (context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.defaultDisplay?.getSize(outSizePoint)
    }

    private fun initTagView() {
        removeAllViewsInLayout()
        for (i in 0 until adapter.getCount()) {
            val view = adapter.createView(this, i)
            tagViewList.add(view)
            addView(view)
        }
    }

    private fun initTagViewPosition() {
        if (adapter.getCount() == 0) return
        val p1 = Math.PI * (3 - sqrt(5.0))
        val p2 = 2.0 / adapter.getCount()

        tagViewList.forEachIndexed { i, _ ->
            val y = i * p2 - 1 + (p2 / 2)
            val r = sqrt(1 - y * y)
            val p3 = i * p1
            val x = cos(p3) * r
            val z = sin(p3) * r

            adapter.bindPosition(i, x, y, z)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val contentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val contentHeight = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var dimensionX =
            if (widthMode == MeasureSpec.EXACTLY) contentWidth
            else outSizePoint.x - marginLeft - marginRight
        var dimensionY =
            if (heightMode == MeasureSpec.EXACTLY) contentHeight
            else outSizePoint.y - marginTop - marginBottom
        if (dimensionX < dimensionY) {
            dimensionY = dimensionX
        } else {
            dimensionX = dimensionY
        }
        setMeasuredDimension(dimensionX, dimensionY)
        measureChildren(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        tagViewList.forEachIndexed { i, _ ->
            setTagPointByIndex(i, adapter.getLocationByIndex(i))
        }
    }

    private fun setTagPointByIndex(index: Int, point: TagPoint) {
        val view = tagViewList[index]
        val x = ((point.x + 1) * width / 2 - view.width / 2).roundToInt()
        val y = ((point.y + 1) * height / 2 - view.height / 2).roundToInt()
        val transform = ((point.z + 2) / 3).toFloat()
        view.scaleX = transform
        view.scaleY = transform
        view.z = transform
        view.alpha = transform
        view.isClickable = point.z > 0
        view.layout(x, y, x + view.measuredWidth, y + view.measuredHeight)
    }

    override fun run() {
        if (!animateFlag) return
        tagViewList.forEachIndexed { index, _ ->
            adapter.updatePointLocationByIndex(index, directionPoint, angle) { point ->
                setTagPointByIndex(index, point)
            }
        }
        postDelayed(this, 50)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }

    fun start() {
        if (animateFlag) return
        animateFlag = true
        post(this)
    }

    fun stop() {
        if (!animateFlag) return
        animateFlag = false
        removeCallbacks(this)
    }

}