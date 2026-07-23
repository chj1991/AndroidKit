package com.sys.androidkit.feature.chartscustom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/** Canvas 雷达图：网格多边形 + 数据填充。 */
class RadarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : BaseChartView(context, attrs) {

    var labels: List<String> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    /** 每条系列的归一化分值 0..100，长度需与 labels 一致 */
    var series: List<ChartSeries> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f * density
    }

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val n = labels.size
        if (n < 3 || width == 0 || height == 0) return

        val cx = width / 2f
        val cy = height / 2f + dp(8f)
        val radius = min(width, height) / 2f - dp(36f)
        val maxValue = 100f

        // 网格
        for (ring in 1..4) {
            val r = radius * ring / 4f
            path.reset()
            for (i in 0 until n) {
                val angle = Math.toRadians(-90.0 + 360.0 * i / n)
                val x = cx + (r * cos(angle)).toFloat()
                val y = cy + (r * sin(angle)).toFloat()
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            canvas.drawPath(path, gridPaint)
        }

        // 轴线 + 标签
        labelPaint.textAlign = Paint.Align.CENTER
        for (i in 0 until n) {
            val angle = Math.toRadians(-90.0 + 360.0 * i / n)
            val x = cx + (radius * cos(angle)).toFloat()
            val y = cy + (radius * sin(angle)).toFloat()
            canvas.drawLine(cx, cy, x, y, axisPaint)
            val lx = cx + ((radius + dp(16f)) * cos(angle)).toFloat()
            val ly = cy + ((radius + dp(16f)) * sin(angle)).toFloat() + labelPaint.textSize / 3f
            canvas.drawText(labels[i], lx, ly, labelPaint)
        }

        series.forEach { s ->
            if (s.points.size != n) return@forEach
            path.reset()
            for (i in 0 until n) {
                val v = (s.points[i].value / maxValue).coerceIn(0f, 1f) * animProgress
                val angle = Math.toRadians(-90.0 + 360.0 * i / n)
                val x = cx + (radius * v * cos(angle)).toFloat()
                val y = cy + (radius * v * sin(angle)).toFloat()
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            fillPaint.color = (s.color and 0x00FFFFFF) or 0x55000000
            strokePaint.color = s.color
            canvas.drawPath(path, fillPaint)
            canvas.drawPath(path, strokePaint)
        }

        // 图例
        var legendX = dp(12f)
        series.forEach { s ->
            fillPaint.color = s.color
            canvas.drawCircle(legendX, dp(14f), dp(4f), fillPaint)
            labelPaint.textAlign = Paint.Align.LEFT
            canvas.drawText(s.name, legendX + dp(8f), dp(14f) + labelPaint.textSize / 3f, labelPaint)
            legendX += labelPaint.measureText(s.name) + dp(28f)
        }
    }
}
