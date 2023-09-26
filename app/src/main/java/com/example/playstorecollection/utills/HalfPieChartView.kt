import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class HalfPieChartView : View {
    private var paint: Paint? = null
    private var rectF: RectF? = null
    private var sweepAngle = 0f

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.style = Paint.Style.FILL
        paint!!.color = resources.getColor(R.color.white) // Set your desired color
        rectF = RectF()
        sweepAngle = 180f // 180 degrees for half-filled
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2
        val centerY = height / 2
        val radius = Math.min(centerX, centerY)
        rectF!![(centerX - radius).toFloat(), (centerY - radius).toFloat(), (centerX + radius).toFloat()] =
            (centerY + radius).toFloat()
        canvas.drawArc(rectF!!, -90f, sweepAngle, true, paint!!)
    }
}