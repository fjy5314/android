package c.example.test.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import c.example.test.R


/**
 * Created by flny on 2018/1/24.
 */
class PinYinSlideView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs), View.OnTouchListener {

    private var textPaint: Paint? = null
    private var backgroundPaint: Paint? = null
    private var circlePaint: Paint? = null
    private var circleTextPaint: Paint? = null
    private var height: Int? = 0
    private var textHeight: Float = 0.toFloat()
    private var paddingHeight: Float = 0.toFloat()
    private var radius: Float = 0.toFloat()
    private var backgroundSize: Float = 0.toFloat()
    private var hasTouch: Boolean = false
    private var lastY: Float = 0.toFloat()
    private var lastX: Float = 0.toFloat()
    private var screenX: Float = 0.toFloat()
    private var screenY: Float = 0.toFloat()
    private var onShowTextListener: OnShowTextListener? = null

    init {
        initView()
    }

    fun setOnShowTextListener(onShowTextListener: OnShowTextListener) {
        this.onShowTextListener = onShowTextListener
    }

    private fun initView() {
        textPaint = Paint()
        textPaint!!.isAntiAlias = true
        textPaint!!.style = Paint.Style.FILL
        textPaint!!.color = resources.getColor(R.color.txtGray)
        val textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, resources.displayMetrics)
        textPaint!!.textSize = textSize
        textPaint!!.textAlign = Paint.Align.CENTER
        val metrics = textPaint!!.fontMetrics
        textHeight = metrics.bottom - metrics.top
        backgroundPaint = Paint()
        backgroundPaint!!.isAntiAlias = true
        backgroundPaint!!.style = Paint.Style.FILL
        backgroundPaint!!.color = resources.getColor(R.color.backgroundGray)
        backgroundSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, resources.displayMetrics)
        radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics)

        circlePaint = Paint()
        circleTextPaint = Paint()
        circlePaint!!.isAntiAlias = true
        circleTextPaint!!.isAntiAlias = true
        circlePaint!!.color = getResources().getColor(R.color.backgroundGray)
        circleTextPaint!!.color = getResources().getColor(R.color.txtGray)
        circlePaint!!.style = Paint.Style.FILL
        circlePaint!!.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 100f, getResources().getDisplayMetrics())
        circleTextPaint!!.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 30f, getResources().getDisplayMetrics())
        circleTextPaint!!.textAlign = Paint.Align.CENTER
        this.setOnTouchListener(this)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        return super.dispatchTouchEvent(event)
    }

    override protected fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        height = MeasureSpec.getSize(heightMeasureSpec)
        paddingHeight = (height!! - 28 * textHeight) / 29
        screenX = (resources.displayMetrics.widthPixels / 2).toFloat()
        screenY = (height!! / 2).toFloat()
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        var size = 0f
        when (mode) {
            MeasureSpec.EXACTLY -> size = MeasureSpec.getSize(widthMeasureSpec).toFloat()
            MeasureSpec.AT_MOST -> size = backgroundSize
        }
        setMeasuredDimension(size.toInt(), height!!)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
                if (lastX > 0 && lastX <= backgroundSize) {
                    hasTouch = true
                    invalidate()
                    requestLayout()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                lastX = event.x
                lastY = event.y
                if (lastX > 0 && lastX <= backgroundSize) {
                    hasTouch = true
                    invalidate()
                    requestLayout()
                }
            }
            MotionEvent.ACTION_UP -> if (hasTouch) {
                hasTouch = false
                invalidate()
            }
        }
        return true
    }

    //    @Override
    //    public boolean onTouchEvent(MotionEvent event) {
    //        float x=0;
    //        switch (event.getAction()){
    //            case MotionEvent.ACTION_DOWN:
    //                lastX=event.getX();
    //                lastY=event.getY();
    //                if (lastX>=0&&lastX<=backgroundSize){
    //                    hasTouch=true;
    //                    invalidate();
    //                    requestLayout();
    //                }
    //                break;
    //            case MotionEvent.ACTION_MOVE:
    //                lastX=event.getX();
    //                lastY=event.getY();
    //                if (lastX>=0&&lastX<=backgroundSize){
    //                    hasTouch=true;
    //                    invalidate();
    //                    requestLayout();
    //                }
    //                break;
    //            case MotionEvent.ACTION_UP:
    //                if (hasTouch){
    //                    hasTouch=false;
    //                    invalidate();
    //                }
    //                break;
    //        }
    //        return true;
    //    }

    override fun onDraw(canvas: Canvas) {
        val c = charArrayOf('↑', 'A', '#')
        var baseY = textHeight
        val baseX = (0 + backgroundSize) / 2
        val radius1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 100f, getResources().getDisplayMetrics()) / 2
        if (hasTouch) {
            val c1 = charArrayOf('↑', 'A', '#')
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect(0.toFloat(), 0.toFloat(), backgroundSize, height!!.toFloat(), radius, radius, backgroundPaint)
            } else {
                canvas.drawRect(0.toFloat(), 0.toFloat(), backgroundSize, height!!.toFloat(), backgroundPaint)
            }
            val offsetY = textHeight + paddingHeight
            for (i in 0..27) {
                if (lastY >= i * offsetY && lastY <= (i + 1) * offsetY) {
                    canvas.drawCircle(screenX, screenY, radius1, circlePaint)
                    if (i == 0) {
                        if (onShowTextListener != null) {
                            onShowTextListener!!.showText(c1[0].toString())
                        }
                        //canvas.drawText(c1,0,1,screenX,screenY+textHeight,circleTextPaint);
                    } else if (i > 0 && i < 27) {
                        if (onShowTextListener != null) {
                            onShowTextListener!!.showText(c1[1].toString())
                        }
                        //canvas.drawText(c1,1,1,screenX,screenY+textHeight,circleTextPaint);
                    } else if (i == 27) {
                        if (onShowTextListener != null) {
                            onShowTextListener!!.showText(c1[2].toString())
                        }
                        //canvas.drawText(c1,2,1,screenX,screenY+textHeight,circleTextPaint);
                    }
                    break
                    //canvas.drawText(c1,0,1,screenX,screenY+textHeight,circleTextPaint);
                }
                if (i in 1..26) {
                    c1[1]++
                }
            }
        } else {
            if (onShowTextListener != null) {
                onShowTextListener!!.showText("")
            }
        }

        for (i in 0..27) {
            when (i) {
                0 -> canvas.drawText(c, 0, 1, baseX, baseY, textPaint)
                in 1..26 -> {
                    canvas.drawText(c, 1, 1, baseX, baseY, textPaint)
                    c[1]++
                }
                27 -> canvas.drawText(c, 2, 1, baseX, baseY, textPaint)
            }
            baseY += paddingHeight + textHeight
        }
    }

    interface OnShowTextListener {
        fun showText(text: String)
    }

}
