package com.sys.androidkit.feature.chartscustom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet

/** Canvas 折线图：多系列、圆点、网格与坐标轴。 */
class LineChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : BaseChartView(context, attrs) {

    var series: List<ChartSeries> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2.5f * density
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val pointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (series.isEmpty() || width == 0 || height == 0) return

        val padL = dp(40f)
        val padR = dp(16f)
        val padT = dp(20f)
        val padB = dp(36f)
        val chartW = width - padL - padR
        val chartH = height - padT - padB
        if (chartW <= 0 || chartH <= 0) return

        val maxPoints = series.maxOf { it.points.size }.coerceAtLeast(1)
        val maxY = series.flatMap { it.points }.maxOfOrNull { it.value }?.coerceAtLeast(1f) ?: 1f
        val labels = series.first().points.map { it.xLabel }

        // 网格 + Y 轴刻度
        val gridLines = 4
        for (i in 0..gridLines) {
            val y = padT + chartH * (1f - i / gridLines.toFloat())
            canvas.drawLine(padL, y, padL + chartW, y, gridPaint)
            val value = maxY * i / gridLines
            labelPaint.textAlign = Paint.Align.RIGHT
            canvas.drawText(
                value.toInt().toString(),
                padL - dp(6f),
                y + labelPaint.textSize / 3f,
                labelPaint,
            )
        }

        // 坐标轴
        canvas.drawLine(padL, padT, padL, padT + chartH, axisPaint)
        canvas.drawLine(padL, padT + chartH, padL + chartW, padT + chartH, axisPaint)

        // X 标签
        labelPaint.textAlign = Paint.Align.CENTER
        labels.forEachIndexed { i, label ->
            val x = padL + chartW * (if (maxPoints == 1) 0.5f else i / (maxPoints - 1f))
            canvas.drawText(label, x, padT + chartH + dp(18f), labelPaint)
        }

        val progressCount = (maxPoints * animProgress).coerceAtLeast(1f)
        series.forEach { s ->
            linePaint.color = s.color
            pointPaint.color = s.color
            path.reset()
            val visible = s.points.take(progressCount.toInt().coerceAtLeast(1))
            visible.forEachIndexed { i, p ->
                val x = padL + chartW * (if (maxPoints == 1) 0.5f else i / (maxPoints - 1f))
                val y = padT + chartH * (1f - (p.value / maxY).coerceIn(0f, 1f))
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            canvas.drawPath(path, linePaint)
            visible.forEachIndexed { i, p ->
                val x = padL + chartW * (if (maxPoints == 1) 0.5f else i / (maxPoints - 1f))
                val y = padT + chartH * (1f - (p.value / maxY).coerceIn(0f, 1f))
                canvas.drawCircle(x, y, dp(3.5f), pointPaint)
            }
        }

        // 图例
        var legendX = padL
        val legendY = dp(14f)
        series.forEach { s ->
            pointPaint.color = s.color
            canvas.drawCircle(legendX, legendY, dp(4f), pointPaint)
            labelPaint.textAlign = Paint.Align.LEFT
            canvas.drawText(s.name, legendX + dp(8f), legendY + labelPaint.textSize / 3f, labelPaint)
            legendX += labelPaint.measureText(s.name) + dp(28f)
        }
    }
}
