package com.sys.androidkit.feature.chartscustom

import kotlin.random.Random

object CustomChartSampleFactory {

    private val colors = intArrayOf(
        0xFF1565C0.toInt(),
        0xFF00897B.toInt(),
        0xFFEF6C00.toInt(),
        0xFF6A1B9A.toInt(),
        0xFFC62828.toInt(),
        0xFF455A64.toInt(),
    )

    fun lineSeries(seed: Int): List<ChartSeries> {
        val r = Random(seed)
        val labels = listOf("一", "二", "三", "四", "五", "六", "日")
        fun series(name: String, color: Int, base: Float) = ChartSeries(
            name = name,
            color = color,
            points = labels.map { ChartPoint(it, base + r.nextFloat() * 40f) },
        )
        return listOf(
            series("访问", colors[0], 35f),
            series("成交", colors[2], 20f),
        )
    }

    fun barPoints(seed: Int): List<ChartPoint> {
        val r = Random(seed)
        return listOf("A", "B", "C", "D", "E", "F").map {
            ChartPoint(it, 25f + r.nextFloat() * 70f)
        }
    }

    fun pieSlices(seed: Int): List<ChartSlice> {
        val r = Random(seed)
        val labels = listOf("Android", "iOS", "Web", "其他")
        return labels.mapIndexed { i, name ->
            ChartSlice(name, 15f + r.nextFloat() * 35f, colors[i])
        }
    }

    fun radar(seed: Int): Pair<List<String>, List<ChartSeries>> {
        val r = Random(seed)
        val labels = listOf("性能", "稳定", "体验", "安全", "扩展")
        fun series(name: String, color: Int) = ChartSeries(
            name = name,
            color = color,
            points = labels.map { ChartPoint(it, 40f + r.nextFloat() * 55f) },
        )
        return labels to listOf(
            series("产品 A", colors[0]),
            series("产品 B", colors[2]),
        )
    }
}
