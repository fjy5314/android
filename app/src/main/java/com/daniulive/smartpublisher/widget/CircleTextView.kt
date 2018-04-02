package com.daniulive.smartpublisher.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.TypedValue
import android.util.AttributeSet
import android.view.View
import com.daniulive.smartpublisher.R


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
        circlePaint!!.isAntiAlias = true
        circleTextPaint!!.isAntiAlias = true
        circlePaint!!.color = resources.getColor(R.color.colorWhite)
        circleTextPaint!!.color = resources.getColor(R.color.colorFont2)
        circlePaint!!.style = Paint.Style.FILL
        circlePaint!!.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 100f, resources.displayMetrics)
        circleTextPaint!!.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 30f, resources.displayMetrics)
        circleTextPaint!!.textAlign = Paint.Align.CENTER
        val metrics = circleTextPaint!!.getFontMetrics()
        textHeight = metrics.bottom - metrics.top
        radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 100f, resources.displayMetrics) / 2
    }

    fun setText(text: String) {
        this.text = text
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(radius.toInt() * 2, radius.toInt() * 2)
    }

    override fun onDraw(canvas: Canvas) {
        if (!text.isNullOrEmpty()) {
            canvas.drawCircle(radius, radius, radius, circlePaint)
            canvas.drawText(text, radius, radius + textHeight / 4, circleTextPaint)
        }
    }
}
