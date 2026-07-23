package com.sys.androidkit.feature.chartscustom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet

/** Canvas 柱状图：圆角柱、网格与数值标签。 */
class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : BaseChartView(context, attrs) {

    var points: List<ChartPoint> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    var barColor: Int = 0xFF1565C0.toInt()
        set(value) {
            field = value
            invalidate()
        }

    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val rect = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (points.isEmpty() || width == 0 || height == 0) return

        val padL = dp(40f)
        val padR = dp(16f)
        val padT = dp(20f)
        val padB = dp(36f)
        val chartW = width - padL - padR
        val chartH = height - padT - padB
        if (chartW <= 0 || chartH <= 0) return

        val maxY = points.maxOf { it.value }.coerceAtLeast(1f)
        val n = points.size
        val slot = chartW / n
        val barWidth = slot * 0.55f

        val gridLines = 4
        for (i in 0..gridLines) {
            val y = padT + chartH * (1f - i / gridLines.toFloat())
            canvas.drawLine(padL, y, padL + chartW, y, gridPaint)
            labelPaint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
                (maxY * i / gridLines).toInt().toString(),
                padL - dp(6f),
                y + labelPaint.textSize / 3f,
                labelPaint,
            )
        }
        canvas.drawLine(padL, padT, padL, padT + chartH, axisPaint)
        canvas.drawLine(padL, padT + chartH, padL + chartW, padT + chartH, axisPaint)

        barPaint.color = barColor
        labelPaint.textAlign = Paint.Align.CENTER
        points.forEachIndexed { i, p ->
            val h = chartH * (p.value / maxY) * animProgress
            val cx = padL + slot * i + slot / 2f
            val left = cx - barWidth / 2f
            val top = padT + chartH - h
            val bottom = padT + chartH
            rect.set(left, top, left + barWidth, bottom)
            canvas.drawRoundRect(rect, dp(6f), dp(6f), barPaint)
            canvas.drawText(p.xLabel, cx, padT + chartH + dp(18f), labelPaint)
            if (animProgress > 0.85f) {
                canvas.drawText(
                    p.value.toInt().toString(),
                    cx,
                    top - dp(4f),
                    labelPaint,
                )
            }
        }
    }
}
