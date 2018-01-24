package c.example.test.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.TypedValue
import android.util.AttributeSet
import android.view.View
import c.example.test.R


/**
 * Created by flny on 2018/1/24.
 */
class CircleTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var circlePaint: Paint? = null
    private var circleTextPaint: Paint? = null
    private var textHeight: Float = 0.toFloat()
    private var text: String? = null
    private var radius: Float = 0.toFloat()

    init {
        initView()
    }

    private fun initView() {
        circlePaint = Paint()
        circleTextPaint = Paint()
        circlePaint!!.setAntiAlias(true)
        circleTextPaint!!.setAntiAlias(true)
        circlePaint!!.setColor(getResources().getColor(R.color.colorWhite))
        circleTextPaint!!.setColor(getResources().getColor(R.color.colorFont2))
        circlePaint!!.setStyle(Paint.Style.FILL)
        circlePaint!!.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 100f, getResources().getDisplayMetrics()))
        circleTextPaint!!.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 30f, getResources().getDisplayMetrics()))
        circleTextPaint!!.setTextAlign(Paint.Align.CENTER)
        val metrics = circleTextPaint!!.getFontMetrics()
        textHeight = metrics.bottom - metrics.top
        radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 100f, getResources().getDisplayMetrics()) / 2
    }

    fun setText(text: String) {
        this.text = text
        invalidate()
    }

    override protected fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(radius.toInt() * 2, radius.toInt() * 2)
    }

    override protected fun onDraw(canvas: Canvas) {
        if (text != null) {
            canvas.drawCircle(radius, radius, radius, circlePaint)
            canvas.drawText(text, radius, radius + textHeight / 4, circleTextPaint)
        }
    }
}
