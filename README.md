# AndroidKit

Android 开发技术点综合 Demo 集合：经典 **View + Jetpack**，多模块工程，**当前版本固定 MVVM**。

目标：用可运行示例沉淀面试与业务中的高频技术点，后续可按模块扩展（含 MVI 对比，不在本版范围）。

---

## 技术选型（当前版本）

| 项 | 选择 |
|---|---|
| UI | View + XML + ViewBinding |
| 语言 | Kotlin |
| 架构 | **固定 MVVM**（ViewModel + StateFlow） |
| 导航 | 单 Activity + Navigation + Fragment |
| 异步 | Coroutines + Flow |
| 本地存储 | Room、DataStore |
| 网络 | OkHttp + Retrofit |
| DI | Hilt 2.59.2（兼容 AGP 9） |
| minSdk | 24 |

**明确不做（本版）**

- Compose
- MVI / 运行时架构切换（预留后续版本）
- 插件化、热修复完整实现

---

## 模块划分

```
AndroidKit/
├── app/                          # 壳：Application、MainActivity、Nav、全局 DI
├── core/
│   ├── common/                   # Result、扩展、日志、协程工具
│   ├── ui/                       # BaseFragment、通用状态视图、主题资源
│   ├── datastore/                # DataStore 封装
│   ├── database/                 # Room
│   └── network/                  # OkHttp / Retrofit 封装
└── feature/
    ├── home/                     # 分类首页、Demo 列表
    ├── settings/                 # 设置、关于
    ├── sample-counter/           # MVVM 状态管理示例（Counter）
    ├── lifecycle/                # 生命周期
    ├── async/                    # 协程 / Flow
    ├── recycler/                 # RecyclerView
    ├── storage/                  # Room / DataStore Demo
    ├── network/                  # Retrofit Demo
    ├── system/                   # 权限 / 通知 / 前台服务
    ├── view-custom/              # 自定义 View
    ├── animation/                # 属性动画
    ├── image/                    # Coil 图片加载
    ├── performance/              # StrictMode / 泄漏场景
    └── compat/                   # 版本行为变更
```

依赖方向（单向）：

```
app → feature:* → core:*
```

`feature` 之间不互相依赖；跨页面跳转由 `app` 的 Navigation 统一编排。

---

## 页面约定

每个技术点 Demo 页建议包含三块：

1. **运行区**：可点击、可观察效果
2. **要点**：原理与关键 API
3. **踩坑**：线程、生命周期、版本差异等

---

## 技术点清单（按模块）

### 1. `app`

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| APP-01 | Application 初始化 | Hilt、日志等 | P0 |
| APP-02 | 单 Activity | `MainActivity` + NavHost | P0 |
| APP-03 | Navigation 集成 | 跨 feature 导航、参数传递 | P0 |
| APP-04 | 深色模式 | 跟随系统 / 手动 | P1 |
| APP-05 | 启动主题 | Splash / Theme / 状态栏 | P1 |

### 2. `core:common`

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| COM-01 | Result / 密封类 | 成功失败统一模型 | P0 |
| COM-02 | 协程工具 | Dispatchers、安全 launch | P0 |
| COM-03 | 扩展函数 | View / Context 等 | P0 |
| COM-04 | 日志封装 | Debug 开关、分级 | P0 |
| COM-05 | 时间 / 格式化工具 | Demo 共用 | P2 |

### 3. `core:ui`

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| UI-01 | BaseFragment | ViewBinding、通用状态 | P0 |
| UI-02 | Demo 列表 Item | 目录卡片、说明区布局 | P0 |
| UI-03 | 主题与资源 | color / typography | P0 |
| UI-04 | 状态视图 | Loading / Empty / Error | P0 |
| UI-05 | 弹窗 / Snackbar 封装 | 统一提示 | P1 |
| UI-06 | 屏幕适配约定 | 尺寸、WindowInsets | P1 |

### 4. `core:datastore`

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| DS-01 | Preferences DataStore | 读写封装 | P0 |
| DS-02 | 收藏 / 最近浏览 | 本地记录 | P1 |
| DS-03 | 主题偏好 | 与设置页联动 | P1 |

### 5. `core:database`

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| DB-01 | Room 基础 | Entity / Dao / Database | P0 |
| DB-02 | 协程 / Flow 查询 | 响应式列表 | P0 |
| DB-03 | Migration | 版本升级示例 | P1 |
| DB-04 | TypeConverter | 复杂字段 | P1 |
| DB-05 | 关系查询 | 一对多（可选） | P2 |

### 6. `core:network`

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| NET-01 | OkHttp 客户端 | 超时、日志拦截器 | P0 |
| NET-02 | Retrofit 封装 | 接口、Converter | P0 |
| NET-03 | 统一错误处理 | HTTP / 业务错误映射 | P0 |
| NET-04 | 请求头拦截器 | 演示向 | P1 |
| NET-05 | 缓存策略 | 基础 Cache | P2 |

### 7. `feature:home`

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| HOME-01 | 分类首页 | 按领域展示入口 | P0 |
| HOME-02 | Demo 列表 | 分类下技术点列表 | P0 |
| HOME-03 | 本地目录数据 | `DemoCatalog` 静态配置 | P0 |
| HOME-04 | 搜索 | 标题 / 标签 / 摘要过滤（已实现） | P1 |
| HOME-05 | 收藏入口 | 跳转收藏列表 | P1 |

### 8. `feature:settings`

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| SET-01 | 设置页骨架 | 主题等入口 | P0 |
| SET-02 | 主题切换 | 浅色 / 深色 / 跟随系统 | P1 |
| SET-03 | 关于页 | 版本、模块说明 | P2 |

### 9. `feature:sample-counter`

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| CTR-01 | Counter MVVM | ViewModel + StateFlow | P0 |
| CTR-02 | 旋转保状态 | 配置变更不丢数 | P0 |
| CTR-03 | 事件与状态分离 | UI 事件 → ViewModel | P0 |

### 10. `feature:lifecycle`

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| LC-01 | Activity 生命周期墙 | 回调日志可视化 | P0 |
| LC-02 | Fragment 生命周期墙 | 与 Activity 对照 | P0 |
| LC-03 | 进程 / 重建场景 | 旋转、后台回收说明 | P1 |
| LC-04 | LifecycleObserver | 自定义观察者 | P1 |

### 11. `feature:async`

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| ASY-01 | 协程基础 | launch / async / 取消 | P0 |
| ASY-02 | Dispatcher 切换 | Main / IO / Default | P0 |
| ASY-03 | Flow 基础 | cold flow 收集 | P0 |
| ASY-04 | StateFlow / SharedFlow | 热流对比 | P0 |
| ASY-05 | 结构化并发 | coroutineScope / supervisorScope / async（已落地） | P1 |
| ASY-06 | WorkManager | 简单一次性任务（已落地） | P2 |

### 12. `feature:recycler`

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| RV-01 | 多 Type 列表 | 标题 / 内容等 | P0 |
| RV-02 | DiffUtil / ListAdapter | 局部刷新 | P0 |
| RV-03 | 点击 / 长按 | 事件回调 | P0 |
| RV-04 | ItemDecoration | 分割线 / 间距 | P1 |
| RV-05 | 侧滑 / 拖拽 | ItemTouchHelper | P2 |

### 13. `feature:storage`

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| ST-01 | Room CRUD | 增删改查列表 | P0 |
| ST-02 | DataStore 读写 | 偏好示例 | P0 |
| ST-03 | SP vs DataStore | 对比说明 | P1 |
| ST-04 | 文件存储路径 | 内部 / 缓存说明 | P2 |

### 14. `feature:network`

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| NW-01 | Retrofit 列表请求 | Loading / Success / Error | P0 |
| NW-02 | 协程挂起接口 | suspend API | P0 |
| NW-03 | 错误态展示 | 超时、4xx/5xx | P0 |
| NW-04 | 下拉刷新 | SwipeRefresh | P1 |
| NW-05 | 上传下载进度 | 可选 | P2 |

---

### 15. `feature:system`（Phase 2 进行中）

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| SYS-01 | 运行时权限 | Activity Result API，相机 / 通知（已实现） | P1 |
| SYS-02 | 通知渠道 | 多渠道创建与发送（已实现） | P1 |
| SYS-03 | Foreground Service | 前台计时服务 + 常驻通知（已实现） | P1 |
| SYS-04 | 广播限制适配 | 动态注册 + 应用内广播（已实现） | P1 |
| SYS-05 | FileProvider 分享 | content:// 分享缓存文件（已实现） | P1 |

### 16. `feature:view-custom`（Phase 2 进行中）

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| VC-01 | 自定义 View 绘制 | RingProgress：Measure / Draw（已实现） | P1 |
| VC-02 | 触摸事件 | 拖动改进度 + 防父布局拦截（已实现） | P1 |
| VC-03 | 滑动冲突 | 竖/横嵌套 + requestDisallowIntercept（已落地） | P1 |

### 17. `feature:animation`（Phase 2 进行中）

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| AN-01 | 属性动画 | ObjectAnimator / ValueAnimator（已实现） | P1 |
| AN-02 | AnimatorSet | 组合缩放透明（已实现） | P1 |
| AN-03 | Transition | TransitionManager / ChangeBounds / AutoTransition（已落地） | P2 |
| AN-04 | Lottie | 本地 JSON 播放 / 暂停 / 调速（已落地） | P2 |
| AN-05 | MotionLayout | MotionScene start/end 过渡（已落地） | P2 |

### 18. `feature:image`（Phase 2 进行中）

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| IMG-01 | Coil 加载 | 成功 / 失败 / 占位图（已实现） | P1 |
| IMG-02 | 缓存策略演示 | 内存/磁盘缓存说明 | P1 |
| IMG-03 | Photo Picker | 系统选择器 + content Uri（已落地） | P2 |
| IMG-04 | 自定义图片选择 | 独立选择页 + Fragment Result 回传；张数默认 1 单选，>1 多选（已落地） | P2 |
| IMG-05 | CameraX | Preview + ImageCapture 拍照（已落地） | P2 |

### 19. `feature:performance`（Phase 2 进行中）

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| PF-01 | StrictMode | 主线程 DiskWrite / 卡顿演示（已实现） | P1 |
| PF-02 | 内存泄漏场景 | 单例持有 Activity 对比修复（已实现） | P1 |
| PF-03 | 启动优化说明 | Application 耗时 + 串行/并行对比（已实现） | P1 |
| PF-04 | Baseline Profile | ProfileInstaller + 生成清单说明（已落地） | P2 |

### 20. `feature:compat`（Phase 2 进行中）

| ID | 技术点 | 说明 | 优先级 |
|----|--------|------|--------|
| CP-01 | Android 10–15 行为变更 | 清单 + 当前设备高亮（已实现） | P1 |
| CP-02 | Scoped Storage | 路径对比 + 应用专属目录写入（已落地） | P1 |
| CP-03 | 后台限制 | 电池优化 / 精确闹钟 / 说明清单（已落地） | P1 |

---

## Phase 2+ 预留模块

| 模块 | 技术点方向 | 优先级 |
|------|------------|--------|
| `:benchmark` | Macrobenchmark 生成 baseline-prof.txt | P2 |
| `architecture:mvi` | Intent / Store / Effect，与 MVVM 对比 | 后续 |
| 架构切换 | 设置中手动切换 MVVM / MVI | 后续 |

---

## Phase 1 范围（优先落地）

```
app                         APP-01 ~ APP-03
core:common                 COM-01 ~ COM-04
core:ui                     UI-01 ~ UI-04
core:datastore              DS-01
core:database               DB-01 ~ DB-02
core:network                NET-01 ~ NET-03
feature:home                HOME-01 ~ HOME-03
feature:settings            SET-01
feature:sample-counter      CTR-01 ~ CTR-03
feature:lifecycle           LC-01 ~ LC-02
feature:async               ASY-01 ~ ASY-06
feature:recycler            RV-01 ~ RV-03
feature:storage             ST-01 ~ ST-02
feature:network             NW-01 ~ NW-03
```

### Phase 1 完成标准

- 多模块工程可编译安装
- 首页可按分类进入各 Demo
- 所有 Demo 统一使用 MVVM
- Counter 旋转后状态保持
- Room / Retrofit 具备成功与失败态展示

---

## 信息架构

```
首页（支持搜索）
 ├─ 架构示例（Counter）
 ├─ 生命周期
 ├─ 异步并发（Coroutine / Flow / WorkManager）
 ├─ UI 界面（… / Transition / Lottie / MotionLayout）
 ├─ 数据存储
 ├─ 网络通信
 ├─ 图片多媒体（Coil / Photo Picker / Custom Picker / CameraX）
 ├─ 系统能力（Permission / Notification / Foreground / FileProvider / Broadcast）
 ├─ 性能优化（StrictMode / Leak / Startup / Baseline Profile）
 ├─ 安全兼容（Compat / Scoped Storage / Background）
 └─ 设置
```

---

## 后续规划（简述）

1. **本版**：多模块 + 固定 MVVM + Phase 1 Demo
2. **下一版**：补齐 Phase 2 系统能力 / 自定义 View / 性能页
3. **再下一版**：引入 `architecture:mvi` 与可切换对比 Demo

---

## 工程状态

Phase 1 完成；Phase 2 主体 Demo 已较完整。`:app:assembleDebug` 可通过。

| 模块 | 状态 |
|------|------|
| `app` | 单 Activity + Navigation + Deep Link + Hilt `AppModule` |
| `core:*` | common / ui / datastore / database / network 已就绪 |
| `feature:home` | 分类首页 + Demo 列表 + 搜索 |
| `feature:settings` | 设置页 + DataStore 主题切换（跟随系统 / 浅色 / 深色） |
| `feature:sample-counter` | Counter MVVM + `@HiltViewModel` |
| `feature:lifecycle` | 生命周期日志墙 |
| `feature:async` | Coroutine / Flow / WorkManager / Structured Concurrency |
| `feature:recycler` | 多 Type + DiffUtil |
| `feature:storage` | Room / DataStore Lab（Dao / Prefs 由 Hilt 注入） |
| `feature:network` | Retrofit 列表请求（`PostApi` 由 Hilt 注入） |
| `feature:system` | Permission / Notification / Foreground / FileProvider / Broadcast |
| `feature:view-custom` | RingProgress；Nested Scroll 滑动冲突 |
| `feature:animation` | 属性动画；Transition；Lottie；MotionLayout |
| `feature:image` | Coil；Photo Picker；自定义选图；CameraX |
| `feature:performance` | StrictMode + Leak + Startup + Baseline Profile |
| `feature:compat` | 行为变更清单；Scoped Storage；后台限制 |

### 运行

```bash
./gradlew :app:assembleDebug
# 或 Android Studio 直接 Run
```

### 导航约定

跨 feature 跳转使用 Deep Link，避免 feature 依赖 `app` 的 `R.id`：

- `androidkit://category/{categoryId}`
- `androidkit://demo/{demoId}`
- `androidkit://settings`

### Hilt 约定

- `AndroidKitApp`：`@HiltAndroidApp`，并监听 DataStore 主题写入 `AppCompatDelegate`
- `MainActivity` / Demo Fragment：`@AndroidEntryPoint`
- ViewModel：`@HiltViewModel` + 构造注入
- 全局绑定：`app/.../di/AppModule.kt`（`AppPreferences` / `AppDatabase` / `NoteDao` / `PostApi`）
