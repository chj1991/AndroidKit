package com.sys.androidkit.feature.home

import com.sys.androidkit.core.common.model.DemoCategory
import com.sys.androidkit.core.common.model.DemoItem

object DemoCatalog {

    const val DEMO_COUNTER = "counter"
    const val DEMO_LIFECYCLE = "lifecycle"
    const val DEMO_LIFECYCLE_OBSERVER = "lifecycle_observer"
    const val DEMO_RECREATION = "recreation"
    const val DEMO_COROUTINE = "coroutine"
    const val DEMO_FLOW = "flow"
    const val DEMO_WORK = "work"
    const val DEMO_STRUCTURED = "structured"
    const val DEMO_RECYCLER = "recycler"
    const val DEMO_TOUCH_HELPER = "touch_helper"
    const val DEMO_NESTED_SCROLL = "nested_scroll"
    const val DEMO_ROOM = "room"
    const val DEMO_DATASTORE = "datastore"
    const val DEMO_SP_VS_DATASTORE = "sp_vs_datastore"
    const val DEMO_MMKV = "mmkv"
    const val DEMO_FILE_PATH = "file_path"
    const val DEMO_RELATION = "relation"
    const val DEMO_PAGING = "paging"
    const val DEMO_RETROFIT = "retrofit"
    const val DEMO_INTERCEPTOR = "interceptor"
    const val DEMO_CACHE = "cache"
    const val DEMO_PROGRESS = "progress"
    const val DEMO_PERMISSION = "permission"
    const val DEMO_BIOMETRIC = "biometric"
    const val DEMO_NOTIFICATION = "notification"
    const val DEMO_CUSTOM_VIEW = "custom_view"
    const val DEMO_COMPONENTS = "components"
    const val DEMO_BASIC_UI = "basic_ui"
    const val DEMO_ANDROID_KTX = "android_ktx"
    const val DEMO_CHARTS = "charts"
    const val DEMO_CHARTS_CUSTOM = "charts_custom"
    const val DEMO_FOREGROUND = "foreground"
    const val DEMO_ANIMATION = "animation"
    const val DEMO_TRANSITION = "transition"
    const val DEMO_LOTTIE = "lottie"
    const val DEMO_MOTION = "motion"
    const val DEMO_IMAGE = "image"
    const val DEMO_COIL_CACHE = "coil_cache"
    const val DEMO_PHOTO_PICKER = "photo_picker"
    const val DEMO_CUSTOM_IMAGE_PICKER = "custom_image_picker"
    const val DEMO_CAMERAX = "camerax"
    const val DEMO_SHARE = "share"
    const val DEMO_BROADCAST = "broadcast"
    const val DEMO_STRICT_MODE = "strict_mode"
    const val DEMO_LEAK = "leak"
    const val DEMO_STARTUP = "startup"
    const val DEMO_BASELINE = "baseline"
    const val DEMO_LOG_VIEWER = "log_viewer"
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
                DemoItem(
                    id = DEMO_LIFECYCLE_OBSERVER,
                    title = "LifecycleObserver Lab",
                    summary = "DefaultLifecycleObserver：Fragment vs viewLifecycle",
                    tags = listOf("Lifecycle", "Observer"),
                ),
                DemoItem(
                    id = DEMO_RECREATION,
                    title = "Recreation Lab",
                    summary = "旋转 / 进程死亡：Fragment vs VM vs SavedState",
                    tags = listOf("Lifecycle", "SavedStateHandle"),
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
                    summary = "launch/取消、Dispatcher、async、超时、yield",
                    tags = listOf("Coroutine", "Dispatcher"),
                ),
                DemoItem(
                    id = DEMO_FLOW,
                    title = "Flow Lab",
                    summary = "Cold/Hot、StateFlow、SharedFlow、操作符",
                    tags = listOf("Flow", "StateFlow", "SharedFlow"),
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
            id = "components",
            title = "组件库",
            description = "系统 Widget、Material 3、BasicUI 与 Kotlin 扩展",
            demos = listOf(
                DemoItem(
                    id = DEMO_COMPONENTS,
                    title = "组件目录",
                    summary = "系统控件 + M3 组件一览与交互示例",
                    tags = listOf("Widget", "Material3", "Components"),
                ),
                DemoItem(
                    id = DEMO_BASIC_UI,
                    title = "BasicUI 组件",
                    summary = "Peakmain/BasicUI：Dialog / Loading / RV / Wheel 等 Wiki 组件",
                    tags = listOf("BasicUI", "Dialog", "RecyclerView"),
                ),
                DemoItem(
                    id = DEMO_ANDROID_KTX,
                    title = "AndroidKtx 扩展",
                    summary = "dengzii/AndroidKtx：View / Context / Activity / Preferences 等常用扩展",
                    tags = listOf("Ktx", "Extension", "Kotlin"),
                ),
            ),
        ),
        DemoCategory(
            id = "charts",
            title = "图表",
            description = "折线 / 柱状 / 饼图等",
            demos = listOf(
                DemoItem(
                    id = DEMO_CHARTS,
                    title = "Chart Lab",
                    summary = "折线、柱状、饼图、雷达、K 线等（MPAndroidChart）",
                    tags = listOf("Chart", "MPAndroidChart"),
                ),
                DemoItem(
                    id = DEMO_CHARTS_CUSTOM,
                    title = "Custom Chart Lab",
                    summary = "Canvas 自绘折线 / 柱状 / 饼图 / 雷达",
                    tags = listOf("Canvas", "CustomView", "Chart"),
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
                    id = DEMO_TOUCH_HELPER,
                    title = "TouchHelper Lab",
                    summary = "ItemDecoration + 拖拽排序 / 侧滑删除",
                    tags = listOf("ItemTouchHelper", "ItemDecoration"),
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
            description = "Room / DataStore / MMKV",
            demos = listOf(
                DemoItem(
                    id = DEMO_ROOM,
                    title = "Room Lab",
                    summary = "完整 CRUD + 搜索 Flow + TypeConverter",
                    tags = listOf("Room", "CRUD", "Flow"),
                ),
                DemoItem(
                    id = DEMO_DATASTORE,
                    title = "DataStore Lab",
                    summary = "Preferences DataStore 读写",
                    tags = listOf("DataStore"),
                ),
                DemoItem(
                    id = DEMO_SP_VS_DATASTORE,
                    title = "SP vs DataStore",
                    summary = "同步 SP 与异步 DataStore 对比",
                    tags = listOf("SharedPreferences", "DataStore"),
                ),
                DemoItem(
                    id = DEMO_MMKV,
                    title = "MMKV Lab",
                    summary = "腾讯 MMKV 封装：String/Int/Boolean",
                    tags = listOf("MMKV", "KV"),
                ),
                DemoItem(
                    id = DEMO_FILE_PATH,
                    title = "File Path Lab",
                    summary = "filesDir / cacheDir / 外部专属目录对比",
                    tags = listOf("File", "Cache", "Storage"),
                ),
                DemoItem(
                    id = DEMO_RELATION,
                    title = "Relation Lab",
                    summary = "Author 1—N Note：增删改关联",
                    tags = listOf("Room", "Relation"),
                ),
                DemoItem(
                    id = DEMO_PAGING,
                    title = "Paging Lab",
                    summary = "Paging 3 + Room PagingSource（多表）",
                    tags = listOf("Paging3", "Room"),
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
                    summary = "封装客户端 + safeApiCall / 404·500·超时 / 监控",
                    tags = listOf("Retrofit", "Error", "Monitor"),
                ),
                DemoItem(
                    id = DEMO_INTERCEPTOR,
                    title = "Interceptor Lab",
                    summary = "Header / 脱敏日志 / Probe / EventListener",
                    tags = listOf("OkHttp", "Interceptor"),
                ),
                DemoItem(
                    id = DEMO_CACHE,
                    title = "Cache Lab",
                    summary = "OkHttp Cache + FORCE_NETWORK / FORCE_CACHE",
                    tags = listOf("OkHttp", "Cache"),
                ),
                DemoItem(
                    id = DEMO_PROGRESS,
                    title = "Progress Lab",
                    summary = "下载 / 上传字节进度回调",
                    tags = listOf("OkHttp", "Progress"),
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
                    summary = "占位/错误/圆角/圆形/灰度/尺寸采样",
                    tags = listOf("Coil", "Transform"),
                ),
                DemoItem(
                    id = DEMO_COIL_CACHE,
                    title = "Coil Cache Lab",
                    summary = "memory / disk CachePolicy 对比",
                    tags = listOf("Coil", "Cache"),
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
                    id = DEMO_BIOMETRIC,
                    title = "Biometric Lab",
                    summary = "指纹/面部登录 + Strong/Weak/凭据回退",
                    tags = listOf("Biometric", "Fingerprint", "Face"),
                ),
                DemoItem(
                    id = DEMO_NOTIFICATION,
                    title = "Notification Lab",
                    summary = "渠道 + 进度条 / BigPicture / 气泡",
                    tags = listOf("Notification", "Bubble", "BigPicture"),
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
                    title = "Leak Lab（LeakCanary）",
                    summary = "制造泄漏 + recreate；debug 集成 LeakCanary",
                    tags = listOf("LeakCanary", "Memory"),
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
                DemoItem(
                    id = DEMO_LOG_VIEWER,
                    title = "Log Viewer（Timber）",
                    summary = "Timber + 应用内日志缓冲 / 过滤 / 复制",
                    tags = listOf("Timber", "Log"),
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
