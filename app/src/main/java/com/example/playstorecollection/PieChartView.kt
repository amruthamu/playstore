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
        Slice("Category 1", 45f, Color.parseColor("#CCE5FF")),
        Slice("Category 2", 30f, Color.parseColor("#CCE5FF")),
        Slice("Category 3", 25f, Color.parseColor("#CCE5FF"))
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return

        val centerX = width / 2f
        val centerY = height / 2f
        var radius = minOf(width, height) / 2.5f // Increase the size of the pie chart

        // Define the border colors (you can customize these)
        val borderColors = listOf(Color.parseColor("#CCE5FF"), Color.parseColor("#CCE5FF"), Color.BLUE, Color.parseColor("#CCE5FF"))

        for (i in 0 until borderColors.size) {
            // Draw each border with a different color and radius
            val borderPaint = Paint()
            borderPaint.color = borderColors[i]
            borderPaint.style = Paint.Style.STROKE
            borderPaint.strokeWidth = 10f + (i * 10f) // Adjust the border width

            if (i == 2) {
                // If it's the blue circle, draw a 75% border with white
                borderPaint.color = Color.BLUE
                val rectF = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
                canvas.drawArc(rectF, 0f, 270f, false, borderPaint)
            } else {
                // For other borders, draw an arc
                val rectF = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
                canvas.drawArc(rectF, 0f, 360f, false, borderPaint)
            }
            radius -= 20f // Decrease the radius for the next border
        }

        var startAngle = 0f

        for (slice in data) {
            val sweepAngle = 360 * (slice.value / 100f)
            drawSlice(canvas, centerX, centerY, radius, startAngle, sweepAngle, slice.color)
            startAngle += sweepAngle
        }

        // Add a number in the middle of the pie chart
        val numberPaint = Paint()
        numberPaint.color = Color.BLACK
        numberPaint.textSize = 38f // Adjust the text size as needed
        numberPaint.textAlign = Paint.Align.CENTER
        val number = "23" // Replace with the number you want to display
        canvas.drawText(number, centerX, centerY, numberPaint)
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






