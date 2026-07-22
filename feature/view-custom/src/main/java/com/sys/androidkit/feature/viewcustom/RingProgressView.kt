package com.sys.androidkit.feature.viewcustom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.min

/**
 * 自定义环形进度：演示 onMeasure / onDraw / 触摸改进度。
 */
class RingProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    var progress: Float = 0.35f
        set(value) {
            field = value.coerceIn(0f, 1f)
            invalidate()
            onProgressChanged?.invoke(field)
        }

    var onProgressChanged: ((Float) -> Unit)? = null

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 28f
        color = 0xFFE0E0E0.toInt()
        strokeCap = Paint.Cap.ROUND
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 28f
        color = 0xFF1565C0.toInt()
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF212121.toInt()
        textAlign = Paint.Align.CENTER
        textSize = 48f
    }

    private val arcBounds = RectF()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desired = (200 * resources.displayMetrics.density).toInt()
        val width = resolveSize(desired, widthMeasureSpec)
        val height = resolveSize(desired, heightMeasureSpec)
        val size = min(width, height)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val pad = trackPaint.strokeWidth / 2f + 8f
        arcBounds.set(pad, pad, width - pad, height - pad)
        canvas.drawArc(arcBounds, -90f, 360f, false, trackPaint)
        canvas.drawArc(arcBounds, -90f, 360f * progress, false, progressPaint)
        val label = "${(progress * 100).toInt()}%"
        val y = height / 2f - (textPaint.descent() + textPaint.ascent()) / 2f
        canvas.drawText(label, width / 2f, y, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                parent.requestDisallowInterceptTouchEvent(true)
                progress = touchToProgress(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun touchToProgress(x: Float, y: Float): Float {
        val cx = width / 2f
        val cy = height / 2f
        val angle = Math.toDegrees(atan2((y - cy).toDouble(), (x - cx).toDouble())).toFloat()
        // atan2: -180..180, 0 at +x; convert to 0..1 starting from top (-90)
        var normalized = (angle + 90f + 360f) % 360f
        return normalized / 360f
    }
}
