package com.sys.androidkit.feature.compat

data class CompatChange(
    val api: Int,
    val codename: String,
    val title: String,
    val summary: String,
)

object CompatCatalog {
    val changes: List<CompatChange> = listOf(
        CompatChange(
            api = 29,
            codename = "Android 10",
            title = "分区存储 Scoped Storage",
            summary = "外部存储访问收紧，优先 MediaStore / SAF，避免直接写公共目录。",
        ),
        CompatChange(
            api = 30,
            codename = "Android 11",
            title = "软件包可见性",
            summary = "查询其他 App 需声明 <queries>；后台位置需单独权限。",
        ),
        CompatChange(
            api = 31,
            codename = "Android 12",
            title = "精确闹钟 / 导出组件 / Splash",
            summary = "SCHEDULE_EXACT_ALARM 受限；组件需显式 android:exported；启动画面 API 变化。",
        ),
        CompatChange(
            api = 33,
            codename = "Android 13",
            title = "通知运行时权限",
            summary = "POST_NOTIFICATIONS 需动态申请；媒体权限拆分为细粒度读写。",
        ),
        CompatChange(
            api = 34,
            codename = "Android 14",
            title = "前台服务类型强制",
            summary = "启动 FGS 必须匹配 foregroundServiceType，并声明对应权限。",
        ),
        CompatChange(
            api = 35,
            codename = "Android 15",
            title = "边到边与后台限制加强",
            summary = "更强调 edge-to-edge；后台启动与部分隐式 Intent 限制继续收紧。",
        ),
    )
}
