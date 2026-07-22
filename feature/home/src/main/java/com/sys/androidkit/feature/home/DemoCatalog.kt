package com.sys.androidkit.feature.home

import com.sys.androidkit.core.common.model.DemoCategory
import com.sys.androidkit.core.common.model.DemoItem

object DemoCatalog {

    const val DEMO_COUNTER = "counter"
    const val DEMO_LIFECYCLE = "lifecycle"
    const val DEMO_COROUTINE = "coroutine"
    const val DEMO_FLOW = "flow"
    const val DEMO_RECYCLER = "recycler"
    const val DEMO_ROOM = "room"
    const val DEMO_DATASTORE = "datastore"
    const val DEMO_RETROFIT = "retrofit"
    const val DEMO_PERMISSION = "permission"
    const val DEMO_NOTIFICATION = "notification"
    const val DEMO_CUSTOM_VIEW = "custom_view"
    const val DEMO_FOREGROUND = "foreground"
    const val DEMO_ANIMATION = "animation"
    const val DEMO_IMAGE = "image"
    const val DEMO_SHARE = "share"

    val categories: List<DemoCategory> = listOf(
        DemoCategory(
            id = "architecture",
            title = "架构示例",
            description = "MVVM 状态管理",
            demos = listOf(
                DemoItem(
                    id = DEMO_COUNTER,
                    title = "Counter（MVVM）",
                    summary = "ViewModel + StateFlow，旋转不丢状态",
                    tags = listOf("MVVM", "ViewModel", "StateFlow"),
                ),
            ),
        ),
        DemoCategory(
            id = "lifecycle",
            title = "生命周期",
            description = "Activity / Fragment 生命周期",
            demos = listOf(
                DemoItem(
                    id = DEMO_LIFECYCLE,
                    title = "Lifecycle Lab",
                    summary = "生命周期回调日志墙",
                    tags = listOf("Lifecycle"),
                ),
            ),
        ),
        DemoCategory(
            id = "async",
            title = "异步并发",
            description = "协程与 Flow",
            demos = listOf(
                DemoItem(
                    id = DEMO_COROUTINE,
                    title = "Coroutine Lab",
                    summary = "launch / 取消 / Dispatcher",
                    tags = listOf("Coroutine"),
                ),
                DemoItem(
                    id = DEMO_FLOW,
                    title = "Flow Lab",
                    summary = "Flow / StateFlow / SharedFlow",
                    tags = listOf("Flow"),
                ),
            ),
        ),
        DemoCategory(
            id = "ui",
            title = "UI 界面",
            description = "列表、自定义 View、动画",
            demos = listOf(
                DemoItem(
                    id = DEMO_RECYCLER,
                    title = "Recycler Lab",
                    summary = "多 Type + DiffUtil + 点击",
                    tags = listOf("RecyclerView", "DiffUtil"),
                ),
                DemoItem(
                    id = DEMO_CUSTOM_VIEW,
                    title = "Custom View Lab",
                    summary = "onMeasure / onDraw / 触摸改进度",
                    tags = listOf("CustomView", "Canvas", "Touch"),
                ),
                DemoItem(
                    id = DEMO_ANIMATION,
                    title = "Animation Lab",
                    summary = "ObjectAnimator / AnimatorSet / ValueAnimator",
                    tags = listOf("Animation", "AnimatorSet"),
                ),
            ),
        ),
        DemoCategory(
            id = "storage",
            title = "数据存储",
            description = "Room / DataStore",
            demos = listOf(
                DemoItem(
                    id = DEMO_ROOM,
                    title = "Room Lab",
                    summary = "增删改查与 Flow 观察",
                    tags = listOf("Room"),
                ),
                DemoItem(
                    id = DEMO_DATASTORE,
                    title = "DataStore Lab",
                    summary = "Preferences DataStore 读写",
                    tags = listOf("DataStore"),
                ),
            ),
        ),
        DemoCategory(
            id = "network",
            title = "网络通信",
            description = "OkHttp / Retrofit",
            demos = listOf(
                DemoItem(
                    id = DEMO_RETROFIT,
                    title = "Retrofit Lab",
                    summary = "列表请求与 Loading/Error",
                    tags = listOf("Retrofit", "OkHttp"),
                ),
            ),
        ),
        DemoCategory(
            id = "media",
            title = "图片多媒体",
            description = "图片加载等",
            demos = listOf(
                DemoItem(
                    id = DEMO_IMAGE,
                    title = "Image Lab（Coil）",
                    summary = "占位图 / 错误图 / 缓存加载",
                    tags = listOf("Coil", "Image"),
                ),
            ),
        ),
        DemoCategory(
            id = "system",
            title = "系统能力",
            description = "权限、通知、分享等系统 API",
            demos = listOf(
                DemoItem(
                    id = DEMO_PERMISSION,
                    title = "Permission Lab",
                    summary = "运行时权限申请与永久拒绝引导",
                    tags = listOf("Permission", "ActivityResult"),
                ),
                DemoItem(
                    id = DEMO_NOTIFICATION,
                    title = "Notification Lab",
                    summary = "多渠道通知与渠道设置页",
                    tags = listOf("Notification", "Channel"),
                ),
                DemoItem(
                    id = DEMO_FOREGROUND,
                    title = "Foreground Service Lab",
                    summary = "前台服务计时 + 常驻通知",
                    tags = listOf("Service", "Foreground"),
                ),
                DemoItem(
                    id = DEMO_SHARE,
                    title = "FileProvider Lab",
                    summary = "content:// 分享缓存文件",
                    tags = listOf("FileProvider", "Share"),
                ),
            ),
        ),
    )

    fun findCategory(id: String): DemoCategory? = categories.find { it.id == id }

    fun findDemo(id: String): DemoItem? = categories.flatMap { it.demos }.find { it.id == id }
}
