package com.sys.androidkit.feature.async

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

data class StructuredConcurrencyUiState(
    val log: String = "对比 coroutineScope 与 supervisorScope：子任务失败时的传播差异",
)

@HiltViewModel
class StructuredConcurrencyLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(StructuredConcurrencyUiState())
    val uiState: StateFlow<StructuredConcurrencyUiState> = _uiState.asStateFlow()

    private val lines = mutableListOf<String>()

    fun runCoroutineScopeDemo() {
        lines.clear()
        append("== coroutineScope：任一子失败会取消兄弟任务 ==")
        launch {
            try {
                coroutineScope {
                    launch {
                        append("child-A 开始（将失败）")
                        delay(300)
                        error("child-A failed")
                    }
                    launch {
                        append("child-B 开始（长任务）")
                        try {
                            delay(1_200)
                            append("child-B 完成（不应看到）")
                        } catch (ce: CancellationException) {
                            append("child-B 被取消（符合预期）")
                            throw ce
                        }
                    }
                }
                append("coroutineScope 正常结束（不应看到）")
            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                append("coroutineScope 抛出: ${t.message}")
            }
            publish()
        }
    }

    fun runSupervisorScopeDemo() {
        lines.clear()
        append("== supervisorScope：子失败默认不连坐兄弟 ==")
        launch {
            supervisorScope {
                val handler = CoroutineExceptionHandler { _, t ->
                    append("child-A 异常被 handler 吃掉: ${t.message}")
                }
                launch(handler) {
                    append("child-A 开始（将失败）")
                    delay(300)
                    error("child-A failed")
                }
                launch {
                    append("child-B 开始（长任务）")
                    delay(800)
                    append("child-B 完成（仍可完成）")
                }
            }
            append("supervisorScope 结束")
            publish()
        }
    }

    fun runAsyncDemo() {
        lines.clear()
        append("== async + await：失败在 await 时抛出 ==")
        launch {
            try {
                coroutineScope {
                    val a = async {
                        delay(200)
                        error("async-A failed")
                    }
                    val b = async {
                        delay(400)
                        "B-ok"
                    }
                    append("等待 await…")
                    try {
                        a.await()
                    } catch (t: Throwable) {
                        if (t is CancellationException) throw t
                        append("await(A) 捕获: ${t.message}")
                    }
                    append("B=${b.await()}")
                }
            } catch (t: Throwable) {
                if (t is CancellationException) throw t
                append("外层: ${t.message}")
            }
            publish()
        }
    }

    private fun append(line: String) {
        lines += line
        publish()
    }

    private fun publish() {
        _uiState.value = StructuredConcurrencyUiState(lines.joinToString("\n"))
    }
}
