package com.sys.androidkit.feature.chartscustom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import kotlin.math.min

/** Canvas 饼图 / 环形图：扇区、中心文案、右侧图例。 */
class PieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : BaseChartView(context, attrs) {

    var slices: List<ChartSlice> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    /** 0 = 实心饼，>0 为环形内半径比例 */
    var holeRatio: Float = 0.55f
        set(value) {
            field = value.coerceIn(0f, 0.85f)
            invalidate()
        }

    var centerText: String = ""
        set(value) {
            field = value
            invalidate()
        }

    private val slicePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val holePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = 0xFFFAFAFA.toInt()
    }

    private val oval = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (slices.isEmpty() || width == 0 || height == 0) return

        val total = slices.sumOf { it.value.toDouble() }.toFloat().coerceAtLeast(0.0001f)
        val legendW = dp(110f)
        val size = min(width - legendW - dp(16f), height - dp(16f))
        val cx = (width - legendW) / 2f
        val cy = height / 2f
        val radius = size / 2f * 0.85f
        oval.set(cx - radius, cy - radius, cx + radius, cy + radius)

        var start = -90f
        val sweepTotal = 360f * animProgress
        var drawn = 0f
        slices.forEach { slice ->
            val fullSweep = 360f * (slice.value / total)
            val remain = (sweepTotal - drawn).coerceAtLeast(0f)
            val sweep = fullSweep.coerceAtMost(remain)
            if (sweep > 0f) {
                slicePaint.color = slice.color
                canvas.drawArc(oval, start, sweep, true, slicePaint)
            }
            start += fullSweep
            drawn += fullSweep
        }

        if (holeRatio > 0f) {
            canvas.drawCircle(cx, cy, radius * holeRatio, holePaint)
            if (centerText.isNotEmpty() && animProgress > 0.5f) {
                titlePaint.textAlign = Paint.Align.CENTER
                canvas.drawText(centerText, cx, cy + titlePaint.textSize / 3f, titlePaint)
                titlePaint.textAlign = Paint.Align.LEFT
            }
        }

        // 图例
        var ly = cy - slices.size * dp(14f)
        val lx = width - legendW + dp(8f)
        slices.forEach { slice ->
            slicePaint.color = slice.color
            canvas.drawCircle(lx, ly, dp(5f), slicePaint)
            labelPaint.textAlign = Paint.Align.LEFT
            val pct = (slice.value / total * 100).toInt()
            canvas.drawText(
                "${slice.label} $pct%",
                lx + dp(12f),
                ly + labelPaint.textSize / 3f,
                labelPaint,
            )
            ly += dp(22f)
        }
    }
}
