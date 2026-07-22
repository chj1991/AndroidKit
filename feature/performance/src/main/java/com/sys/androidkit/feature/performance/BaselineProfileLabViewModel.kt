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
                appendLine("落地清单：")
                appendLine("1. app 依赖 androidx.profileinstaller")
                appendLine("2. 用 Macrobenchmark 生成 baseline-prof.txt")
                appendLine("3. 放到 src/main 或通过 AGP 合并进 APK")
                appendLine("4. 用 StartupTracing / Macrobenchmark 对比前后")
                appendLine()
                appendLine("本工程：已接入 ProfileInstaller；完整基准模块可后续加 :benchmark。")
                appendLine("对比入口：Startup Lab 的串行/并行初始化演示。")
            },
        )
    }
}
