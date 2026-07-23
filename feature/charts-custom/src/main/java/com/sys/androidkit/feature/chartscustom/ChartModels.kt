package com.sys.androidkit.feature.chartscustom

data class ChartSlice(
    val label: String,
    val value: Float,
    val color: Int,
)

data class ChartPoint(
    val xLabel: String,
    val value: Float,
)

data class ChartSeries(
    val name: String,
    val color: Int,
    val points: List<ChartPoint>,
)

enum class CustomChartType(
    val title: String,
    val summary: String,
) {
    LINE("折线图", "Path + 圆点 + 坐标轴（Canvas）"),
    BAR("柱状图", "圆角柱 + 分组/单系列"),
    PIE("饼图", "扇区 + 环形洞 + 图例"),
    RADAR("雷达图", "多边形网格 + 填充"),
}
