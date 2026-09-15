package com.gridcam.pro.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Custom view that draws grid lines over the camera preview.
 * Grid is visible during preview and recording but not in final output.
 */
class GridOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val gridPaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 2f
        style = Paint.Style.STROKE
        alpha = 180 // Semi-transparent
    }

    var isVisibleGrid = true
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        if (!isVisibleGrid) return

        val width = width.toFloat()
        val height = height.toFloat()

        // Draw vertical lines (divide into 3 equal parts)
        val thirdWidth = width / 3
        for (i in 1..2) {
            val x = i * thirdWidth
            canvas.drawLine(x, 0f, x, height, gridPaint)
        }

        // Draw horizontal lines (divide into 3 equal parts)
        val thirdHeight = height / 3
        for (i in 1..2) {
            val y = i * thirdHeight
            canvas.drawLine(0f, y, width, y, gridPaint)
        }
    }
}
