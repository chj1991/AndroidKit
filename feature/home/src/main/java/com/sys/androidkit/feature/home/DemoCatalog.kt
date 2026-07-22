package com.sys.androidkit.feature.home

import com.sys.androidkit.core.common.model.DemoCategory
import com.sys.androidkit.core.common.model.DemoItem

object DemoCatalog {

    const val DEMO_COUNTER = "counter"
    const val DEMO_LIFECYCLE = "lifecycle"
    const val DEMO_COROUTINE = "coroutine"
    const val DEMO_FLOW = "flow"
    const val DEMO_WORK = "work"
    const val DEMO_STRUCTURED = "structured"
    const val DEMO_RECYCLER = "recycler"
    const val DEMO_NESTED_SCROLL = "nested_scroll"
    const val DEMO_ROOM = "room"
    const val DEMO_DATASTORE = "datastore"
    const val DEMO_RETROFIT = "retrofit"
    const val DEMO_PERMISSION = "permission"
    const val DEMO_NOTIFICATION = "notification"
    const val DEMO_CUSTOM_VIEW = "custom_view"
    const val DEMO_FOREGROUND = "foreground"
    const val DEMO_ANIMATION = "animation"
    const val DEMO_TRANSITION = "transition"
    const val DEMO_LOTTIE = "lottie"
    const val DEMO_MOTION = "motion"
    const val DEMO_IMAGE = "image"
    const val DEMO_PHOTO_PICKER = "photo_picker"
    const val DEMO_CUSTOM_IMAGE_PICKER = "custom_image_picker"
    const val DEMO_CAMERAX = "camerax"
    const val DEMO_SHARE = "share"
    const val DEMO_BROADCAST = "broadcast"
    const val DEMO_STRICT_MODE = "strict_mode"
    const val DEMO_LEAK = "leak"
    const val DEMO_STARTUP = "startup"
    const val DEMO_BASELINE = "baseline"
    const val DEMO_COMPAT = "compat"
    const val DEMO_SCOPED_STORAGE = "scoped_storage"
    const val DEMO_BACKGROUND_LIMIT = "background_limit"

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
            description = "协程、Flow 与 WorkManager",
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
                DemoItem(
                    id = DEMO_WORK,
                    title = "WorkManager Lab",
                    summary = "OneTimeWork + UniqueWork + WorkInfo",
                    tags = listOf("WorkManager", "Background"),
                ),
                DemoItem(
                    id = DEMO_STRUCTURED,
                    title = "Structured Concurrency",
                    summary = "coroutineScope / supervisorScope / async",
                    tags = listOf("Coroutine", "supervisorScope"),
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
                    id = DEMO_NESTED_SCROLL,
                    title = "Nested Scroll Lab",
                    summary = "竖/横滑动冲突与 requestDisallowIntercept",
                    tags = listOf("Touch", "ScrollConflict"),
                ),
                DemoItem(
                    id = DEMO_ANIMATION,
                    title = "Animation Lab",
                    summary = "ObjectAnimator / AnimatorSet / ValueAnimator",
                    tags = listOf("Animation", "AnimatorSet"),
                ),
                DemoItem(
                    id = DEMO_TRANSITION,
                    title = "Transition Lab",
                    summary = "TransitionManager / ChangeBounds / AutoTransition",
                    tags = listOf("Transition", "ChangeBounds"),
                ),
                DemoItem(
                    id = DEMO_LOTTIE,
                    title = "Lottie Lab",
                    summary = "播放 / 暂停 / 调速本地 JSON",
                    tags = listOf("Lottie", "Animation"),
                ),
                DemoItem(
                    id = DEMO_MOTION,
                    title = "MotionLayout Lab",
                    summary = "MotionScene start/end 布局过渡",
                    tags = listOf("MotionLayout", "ConstraintLayout"),
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
            description = "图片加载、选择与相机",
            demos = listOf(
                DemoItem(
                    id = DEMO_IMAGE,
                    title = "Image Lab（Coil）",
                    summary = "占位图 / 错误图 / 缓存加载",
                    tags = listOf("Coil", "Image"),
                ),
                DemoItem(
                    id = DEMO_PHOTO_PICKER,
                    title = "Photo Picker Lab",
                    summary = "系统选择器 + content Uri（通常无权限）",
                    tags = listOf("PhotoPicker", "ActivityResult"),
                ),
                DemoItem(
                    id = DEMO_CUSTOM_IMAGE_PICKER,
                    title = "Custom Image Picker",
                    summary = "独立选图页；确认后返回展示（单/多选）",
                    tags = listOf("MediaStore", "FragmentResult"),
                ),
                DemoItem(
                    id = DEMO_CAMERAX,
                    title = "CameraX Lab",
                    summary = "Preview + ImageCapture 拍照到缓存",
                    tags = listOf("CameraX", "Preview"),
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
                DemoItem(
                    id = DEMO_BROADCAST,
                    title = "Broadcast Lab",
                    summary = "动态注册与应用内广播",
                    tags = listOf("Broadcast", "Receiver"),
                ),
            ),
        ),
        DemoCategory(
            id = "performance",
            title = "性能优化",
            description = "StrictMode、泄漏与启动",
            demos = listOf(
                DemoItem(
                    id = DEMO_STRICT_MODE,
                    title = "StrictMode Lab",
                    summary = "主线程 IO / 卡顿违规检测",
                    tags = listOf("StrictMode", "Performance"),
                ),
                DemoItem(
                    id = DEMO_LEAK,
                    title = "Leak Lab",
                    summary = "单例错误持有 Activity 的对比修复",
                    tags = listOf("Leak", "Context"),
                ),
                DemoItem(
                    id = DEMO_STARTUP,
                    title = "Startup Lab",
                    summary = "Application 耗时与串行/并行初始化对比",
                    tags = listOf("Startup", "Application"),
                ),
                DemoItem(
                    id = DEMO_BASELINE,
                    title = "Baseline Profile Lab",
                    summary = "ProfileInstaller 与生成清单说明",
                    tags = listOf("BaselineProfile", "Startup"),
                ),
            ),
        ),
        DemoCategory(
            id = "compat",
            title = "安全兼容",
            description = "Android 10–15 行为变更",
            demos = listOf(
                DemoItem(
                    id = DEMO_COMPAT,
                    title = "Compat Lab",
                    summary = "版本行为变更清单与当前设备高亮",
                    tags = listOf("Compat"),
                ),
                DemoItem(
                    id = DEMO_SCOPED_STORAGE,
                    title = "Scoped Storage Lab",
                    summary = "应用专属目录与共享存储边界",
                    tags = listOf("ScopedStorage", "Storage"),
                ),
                DemoItem(
                    id = DEMO_BACKGROUND_LIMIT,
                    title = "Background Limit Lab",
                    summary = "电池优化 / 精确闹钟 / 后台限制",
                    tags = listOf("Background", "Battery"),
                ),
            ),
        ),
    )

    fun findCategory(id: String): DemoCategory? = categories.find { it.id == id }

    fun findDemo(id: String): DemoItem? = categories.flatMap { it.demos }.find { it.id == id }
}
