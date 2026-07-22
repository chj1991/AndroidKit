package com.sys.androidkit.feature.performance

import com.sys.androidkit.core.common.startup.StartupTrace
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.coroutineScope

data class StartupUiState(
    val appCreateCostMs: Long = StartupTrace.appCreateCostMs,
    val simMessage: String = "对比串行 / 并行初始化耗时（模拟）",
    val running: Boolean = false,
)

@HiltViewModel
class StartupLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(StartupUiState())
    val uiState: StateFlow<StartupUiState> = _uiState.asStateFlow()

    fun refreshAppCost() {
        _uiState.value = _uiState.value.copy(appCreateCostMs = StartupTrace.appCreateCostMs)
    }

    fun runSerialInit() {
        if (_uiState.value.running) return
        launch {
            _uiState.value = _uiState.value.copy(running = true, simMessage = "串行初始化中…")
            val start = System.currentTimeMillis()
            withContext(Dispatchers.Default) {
                simulateTask("日志", 80)
                simulateTask("配置", 120)
                simulateTask("预热", 150)
            }
            val cost = System.currentTimeMillis() - start
            _uiState.value = StartupUiState(
                appCreateCostMs = StartupTrace.appCreateCostMs,
                simMessage = "串行完成：约 ${cost}ms（任务相加）",
                running = false,
            )
        }
    }

    fun runParallelInit() {
        if (_uiState.value.running) return
        launch {
            _uiState.value = _uiState.value.copy(running = true, simMessage = "并行初始化中…")
            val start = System.currentTimeMillis()
            withContext(Dispatchers.Default) {
                coroutineScope {
                    listOf(
                        async { simulateTask("日志", 80) },
                        async { simulateTask("配置", 120) },
                        async { simulateTask("预热", 150) },
                    ).awaitAll()
                }
            }
            val cost = System.currentTimeMillis() - start
            _uiState.value = StartupUiState(
                appCreateCostMs = StartupTrace.appCreateCostMs,
                simMessage = "并行完成：约 ${cost}ms（接近最慢任务）",
                running = false,
            )
        }
    }

    private suspend fun simulateTask(name: String, delayMs: Long) {
        delay(delayMs)
        // name kept for readability in potential future logs
        name.hashCode()
    }
}
