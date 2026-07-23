package com.sys.androidkit.feature.performance

import com.sys.androidkit.core.common.startup.StartupTrace
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class BaselineProfileUiState(
    val report: String = "",
)

@HiltViewModel
class BaselineProfileLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(BaselineProfileUiState())
    val uiState: StateFlow<BaselineProfileUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val appCost = StartupTrace.appCreateCostMs
        _uiState.value = BaselineProfileUiState(
            report = buildString {
                appendLine(
                    "本次 Application.onCreate ≈ " +
                        if (appCost >= 0) "$appCost ms" else "未知",
                )
                appendLine()
                appendLine("Baseline Profile 是什么？")
                appendLine("预编译关键路径（AOT），降低冷启动与首帧卡顿。")
                appendLine()
                appendLine("本工程已具备：")
                appendLine("1. app 依赖 androidx.profileinstaller")
                appendLine("2. <profileable android:shell=\"true\"/>")
                appendLine("3. :benchmark 模块（StartupBenchmark）")
                appendLine("4. app/benchmark buildType")
                appendLine()
                appendLine("设备上跑基准：")
                appendLine("./gradlew :benchmark:connectedBenchmarkAndroidTest")
                appendLine()
                appendLine("生成 baseline-prof.txt 后放到 app/src/main，")
                appendLine("或用 Baseline Profile Gradle Plugin 自动合并。")
                appendLine("对比入口：Startup Lab 的串行/并行初始化演示。")
            },
        )
    }
}
