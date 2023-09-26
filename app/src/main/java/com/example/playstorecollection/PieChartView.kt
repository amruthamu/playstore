package com.example.playstorecollection

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class PieChartView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val data = listOf(
        Slice("Category 1", 45f, Color.WHITE),
        Slice("Category 2", 30f, Color.WHITE),
        Slice("Category 3", 25f, Color.WHITE)
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = minOf(width, height) / 2f

        // Draw the outer border
        val borderPaint = Paint()
        borderPaint.color = Color.BLUE
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = 10f
        canvas.drawCircle(centerX, centerY, radius, borderPaint)

        // Define the size of the hole in the center
        val holeRadius = radius / 2f

        // Draw the hole in the center
        val holePaint = Paint()
        holePaint.color = Color.WHITE
        holePaint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, holeRadius, holePaint)

        var startAngle = 0f

        for (slice in data) {
            val sweepAngle = 360 * (slice.value / 100f)
            drawSlice(canvas, centerX, centerY, radius, startAngle, sweepAngle, slice.color)
            startAngle += sweepAngle
        }
    }

    private fun drawSlice(canvas: Canvas, centerX: Float, centerY: Float, radius: Float, startAngle: Float, sweepAngle: Float, color: Int) {
        val paint = Paint()
        paint.color = color
        paint.style = Paint.Style.FILL
        val rectF = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        canvas.drawArc(rectF, startAngle, sweepAngle, true, paint)
    }

    data class Slice(val label: String, val value: Float, val color: Int)
}

