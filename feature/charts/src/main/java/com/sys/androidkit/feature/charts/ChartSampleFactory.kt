package com.sys.androidkit.feature.charts

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.BubbleChart
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BubbleData
import com.github.mikephil.charting.data.BubbleDataSet
import com.github.mikephil.charting.data.BubbleEntry
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlin.random.Random

/**
 * 使用 MPAndroidChart 构建各类型演示图。
 */
object ChartSampleFactory {

    private val palette = intArrayOf(
        Color.parseColor("#1565C0"),
        Color.parseColor("#00897B"),
        Color.parseColor("#EF6C00"),
        Color.parseColor("#6A1B9A"),
        Color.parseColor("#C62828"),
        Color.parseColor("#455A64"),
    )

    fun create(context: Context, type: ChartType, seed: Int): Chart<*> {
        val chart = when (type) {
            ChartType.LINE -> buildLine(context, seed)
            ChartType.BAR -> buildBar(context, seed)
            ChartType.HORIZONTAL_BAR -> buildHorizontalBar(context, seed)
            ChartType.PIE -> buildPie(context, seed)
            ChartType.RADAR -> buildRadar(context, seed)
            ChartType.SCATTER -> buildScatter(context, seed)
            ChartType.BUBBLE -> buildBubble(context, seed)
            ChartType.CANDLE -> buildCandle(context, seed)
            ChartType.COMBINED -> buildCombined(context, seed)
        }
        chart.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
        chart.description.isEnabled = false
        chart.setNoDataText("暂无数据")
        chart.legend.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            orientation = Legend.LegendOrientation.HORIZONTAL
            setDrawInside(false)
        }
        chart.animateY(700, Easing.EaseInOutQuad)
        return chart
    }

    private fun rng(seed: Int) = Random(seed)

    private fun buildLine(context: Context, seed: Int): LineChart {
        val r = rng(seed)
        val chart = LineChart(context)
        val sets = mutableListOf<ILineDataSet>()
        listOf("系列 A", "系列 B").forEachIndexed { index, label ->
            val entries = (0 until 8).map { i ->
                Entry(i.toFloat(), 20f + r.nextFloat() * 60f + index * 8f)
            }
            sets += LineDataSet(entries, label).apply {
                color = palette[index]
                setCircleColor(palette[index])
                lineWidth = 2.2f
                circleRadius = 3.5f
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawFilled(index == 0)
                fillColor = palette[index]
                fillAlpha = 50
            }
        }
        chart.data = LineData(sets)
        chart.axisRight.isEnabled = false
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.granularity = 1f
        return chart
    }

    private fun buildBar(context: Context, seed: Int): BarChart {
        val r = rng(seed)
        val chart = BarChart(context)
        val labels = listOf("一", "二", "三", "四", "五", "六")
        val set1 = BarDataSet(
            labels.indices.map { i -> BarEntry(i.toFloat(), 30f + r.nextFloat() * 50f) },
            "销量",
        ).apply {
            color = palette[0]
            setDrawValues(false)
        }
        val set2 = BarDataSet(
            labels.indices.map { i -> BarEntry(i.toFloat(), 20f + r.nextFloat() * 40f) },
            "库存",
        ).apply {
            color = palette[1]
            setDrawValues(false)
        }
        val groupSpace = 0.2f
        val barSpace = 0.05f
        val barWidth = 0.35f
        chart.data = BarData(set1, set2).apply {
            this.barWidth = barWidth
        }
        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = IndexAxisValueFormatter(labels)
            granularity = 1f
            setCenterAxisLabels(true)
            axisMinimum = 0f
            axisMaximum = labels.size.toFloat()
        }
        chart.groupBars(0f, groupSpace, barSpace)
        chart.axisRight.isEnabled = false
        return chart
    }

    private fun buildHorizontalBar(context: Context, seed: Int): HorizontalBarChart {
        val r = rng(seed)
        val chart = HorizontalBarChart(context)
        val labels = listOf("Kotlin", "Java", "Compose", "XML")
        val set = BarDataSet(
            labels.indices.map { i -> BarEntry(i.toFloat(), 40f + r.nextFloat() * 55f) },
            "偏好",
        ).apply {
            colors = palette.toList()
            setDrawValues(true)
            valueTextSize = 10f
        }
        chart.data = BarData(set).apply { barWidth = 0.55f }
        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = IndexAxisValueFormatter(labels)
            granularity = 1f
            setDrawGridLines(false)
        }
        chart.axisRight.isEnabled = false
        return chart
    }

    private fun buildPie(context: Context, seed: Int): PieChart {
        val r = rng(seed)
        val chart = PieChart(context)
        val entries = listOf(
            PieEntry(25f + r.nextFloat() * 20f, "Android"),
            PieEntry(20f + r.nextFloat() * 15f, "iOS"),
            PieEntry(15f + r.nextFloat() * 15f, "Web"),
            PieEntry(10f + r.nextFloat() * 15f, "其他"),
        )
        val set = PieDataSet(entries, "").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList() + palette.toList()
            sliceSpace = 2f
            valueTextSize = 12f
            valueTextColor = Color.WHITE
        }
        chart.data = PieData(set).apply {
            setValueFormatter(PercentFormatter(chart))
        }
        chart.setUsePercentValues(true)
        chart.isDrawHoleEnabled = true
        chart.holeRadius = 48f
        chart.transparentCircleRadius = 52f
        chart.centerText = "份额"
        chart.setCenterTextSize(14f)
        chart.setEntryLabelColor(Color.DKGRAY)
        return chart
    }

    private fun buildRadar(context: Context, seed: Int): RadarChart {
        val r = rng(seed)
        val chart = RadarChart(context)
        val labels = listOf("性能", "稳定", "体验", "安全", "扩展")
        fun series(label: String, color: Int) = RadarDataSet(
            labels.map { RadarEntry(40f + r.nextFloat() * 55f) },
            label,
        ).apply {
            this.color = color
            fillColor = color
            setDrawFilled(true)
            fillAlpha = 80
            lineWidth = 1.8f
            setDrawValues(false)
        }
        chart.data = RadarData(series("产品 A", palette[0]), series("产品 B", palette[2]))
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        chart.yAxis.axisMinimum = 0f
        chart.yAxis.axisMaximum = 100f
        chart.webLineWidth = 1f
        chart.webColor = Color.LTGRAY
        return chart
    }

    private fun buildScatter(context: Context, seed: Int): ScatterChart {
        val r = rng(seed)
        val chart = ScatterChart(context)
        val entries = (0 until 24).map {
            Entry(r.nextFloat() * 10f, r.nextFloat() * 80f)
        }
        chart.data = ScatterData(
            ScatterDataSet(entries, "样本").apply {
                color = palette[3]
                scatterShapeSize = 10f
                setScatterShape(ScatterChart.ScatterShape.CIRCLE)
                setDrawValues(false)
            },
        )
        chart.axisRight.isEnabled = false
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        return chart
    }

    private fun buildBubble(context: Context, seed: Int): BubbleChart {
        val r = rng(seed)
        val chart = BubbleChart(context)
        val entries = (0 until 10).map { i ->
            BubbleEntry(i.toFloat(), 20f + r.nextFloat() * 60f, 5f + r.nextFloat() * 20f)
        }
        chart.data = BubbleData(
            BubbleDataSet(entries, "气泡").apply {
                colors = ColorTemplate.COLORFUL_COLORS.toList()
                setDrawValues(false)
                setNormalizeSizeEnabled(true)
            },
        )
        chart.axisRight.isEnabled = false
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        return chart
    }

    private fun buildCandle(context: Context, seed: Int): CandleStickChart {
        val r = rng(seed)
        val chart = CandleStickChart(context)
        var price = 100f
        val entries = (0 until 12).map { i ->
            val open = price
            val close = open + (r.nextFloat() - 0.45f) * 12f
            val high = maxOf(open, close) + r.nextFloat() * 4f
            val low = minOf(open, close) - r.nextFloat() * 4f
            price = close
            CandleEntry(i.toFloat(), high, low, open, close)
        }
        chart.data = CandleData(
            CandleDataSet(entries, "OHLC").apply {
                shadowColor = Color.DKGRAY
                shadowWidth = 0.8f
                decreasingColor = Color.parseColor("#C62828")
                decreasingPaintStyle = android.graphics.Paint.Style.FILL
                increasingColor = Color.parseColor("#2E7D32")
                increasingPaintStyle = android.graphics.Paint.Style.FILL
                neutralColor = Color.BLUE
                setDrawValues(false)
            },
        )
        chart.axisRight.isEnabled = false
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        return chart
    }

    private fun buildCombined(context: Context, seed: Int): CombinedChart {
        val r = rng(seed)
        val chart = CombinedChart(context)
        chart.drawOrder = arrayOf(
            CombinedChart.DrawOrder.BAR,
            CombinedChart.DrawOrder.LINE,
        )
        val barEntries = (0 until 7).map { i ->
            BarEntry(i.toFloat(), 30f + r.nextFloat() * 40f)
        }
        val lineEntries = (0 until 7).map { i ->
            Entry(i.toFloat(), 40f + r.nextFloat() * 35f)
        }
        val combined = CombinedData().apply {
            setData(
                BarData(
                    BarDataSet(barEntries, "柱").apply {
                        color = palette[1]
                        setDrawValues(false)
                    },
                ).apply { barWidth = 0.45f },
            )
            setData(
                LineData(
                    LineDataSet(lineEntries, "线").apply {
                        color = palette[4]
                        lineWidth = 2.2f
                        setCircleColor(palette[4])
                        setDrawValues(false)
                    },
                ),
            )
        }
        chart.data = combined
        chart.axisRight.isEnabled = false
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.granularity = 1f
        return chart
    }
}
