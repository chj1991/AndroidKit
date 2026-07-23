package com.sys.androidkit.feature.chartscustom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat

/**
 * 自定义图表基类：统一边距、文字画笔、入场动画进度 [animProgress]。
 */
abstract class BaseChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    protected val density = resources.displayMetrics.density

    protected val axisPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFBDBDBD.toInt()
        strokeWidth = 1.5f * density
        style = Paint.Style.STROKE
    }

    protected val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFE0E0E0.toInt()
        strokeWidth = 1f * density
        style = Paint.Style.STROKE
    }

    protected val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF616161.toInt()
        textSize = 11f * density
        textAlign = Paint.Align.CENTER
    }

    protected val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF212121.toInt()
        textSize = 12f * density
        textAlign = Paint.Align.LEFT
        isFakeBoldText = true
    }

    /** 0f..1f，控制绘制显现动画 */
    protected var animProgress: Float = 1f
        private set

    private var animator: ValueAnimator? = null

    protected fun dp(value: Float): Float = value * density

    fun replayAnimation(durationMs: Long = 700L) {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = durationMs
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                animProgress = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onDetachedFromWindow() {
        animator?.cancel()
        super.onDetachedFromWindow()
    }

    protected fun resolveColor(index: Int): Int {
        val colors = intArrayOf(
            0xFF1565C0.toInt(),
            0xFF00897B.toInt(),
            0xFFEF6C00.toInt(),
            0xFF6A1B9A.toInt(),
            0xFFC62828.toInt(),
            0xFF455A64.toInt(),
        )
        return colors[index % colors.size]
    }

    protected fun primaryTextColor(): Int =
        ContextCompat.getColor(context, android.R.color.primary_text_light)
}
