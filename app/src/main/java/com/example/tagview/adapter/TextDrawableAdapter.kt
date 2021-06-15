package com.example.tagview.adapter

import android.graphics.*
import android.text.TextPaint
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DimenRes
import com.example.a3dtagfromios.R
import kotlin.math.roundToInt

/**
 * @author kun
 * @since 2021-Jun-15
 */
class TextDrawableAdapter(
    data: List<String>,
    @DimenRes val textSizeRes: Int = R.dimen.text_size_8
) : TagAdapter<String, ImageView>(data) {
    override fun createView(parent: ViewGroup, position: Int): ImageView {
        val view = ImageView(parent.context)

        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = parent.context.resources.getDimensionPixelSize(textSizeRes).toFloat()
            color = Color.BLACK
            style = Paint.Style.FILL_AND_STROKE
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
        }
        val boundsRect = Rect()
        textPaint.getTextBounds(data[position], 0, data[position].length, boundsRect)

        val fontMetrics = textPaint.fontMetrics
        val textHeight = -fontMetrics.ascent + fontMetrics.descent

        val bitmap = Bitmap.createBitmap(
            boundsRect.width(),
            textHeight.roundToInt(),
            Bitmap.Config.ARGB_8888
        ).apply {
            eraseColor(Color.TRANSPARENT)
        }
        val canvas = Canvas(bitmap)
        canvas.drawText(
            data[position],
            (boundsRect.width() / 2).toFloat(),
            boundsRect.height().toFloat(),
            textPaint
        )
        view.setImageBitmap(bitmap)
        return view
    }
}