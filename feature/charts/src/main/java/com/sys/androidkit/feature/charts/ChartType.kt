package com.sys.androidkit.feature.charts

enum class ChartType(
    val id: String,
    val title: String,
    val summary: String,
) {
    LINE("line", "折线图", "LineChart · 多系列 / 圆点 / 填充"),
    BAR("bar", "柱状图", "BarChart · 分组柱"),
    HORIZONTAL_BAR("hbar", "条形图", "HorizontalBarChart"),
    PIE("pie", "饼图", "PieChart · 环形洞"),
    RADAR("radar", "雷达图", "RadarChart · 多维对比"),
    SCATTER("scatter", "散点图", "ScatterChart"),
    BUBBLE("bubble", "气泡图", "BubbleChart"),
    CANDLE("candle", "K 线图", "CandleStickChart"),
    COMBINED("combined", "组合图", "CombinedChart · 柱+线"),
}
