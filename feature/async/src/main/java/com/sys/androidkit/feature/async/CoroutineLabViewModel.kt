package com.sys.androidkit.feature.async

import com.sys.androidkit.core.common.time.DateFormats
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.yield

data class CoroutineUiState(
    val log: String = "选择下方按钮体验协程用法",
)

@HiltViewModel
class CoroutineLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(CoroutineUiState())
    val uiState: StateFlow<CoroutineUiState> = _uiState.asStateFlow()

    private val lines = mutableListOf<String>()
    private var longJob: Job? = null

    fun clearLog() {
        lines.clear()
        publish("日志已清空")
    }

    /** launch + 协作式取消 */
    fun runLaunchCancel() {
        lines.clear()
        append("== launch / cancel ==")
        longJob?.cancel()
        longJob = launch {
            append("任务启动（每 300ms 步进，共 8 步）")
            try {
                repeat(8) { step ->
                    if (!isActive) return@repeat
                    delay(300)
                    append("step ${step + 1}/8 @ ${threadName()}")
                }
                append("任务自然完成")
            } catch (ce: CancellationException) {
                append("捕获 CancellationException（协作取消）")
                throw ce
            }
            publish()
        }
        publish()
    }

    fun cancelLongJob() {
        if (longJob?.isActive == true) {
            append("调用 job.cancel()")
            longJob?.cancel()
            publish("已请求取消")
        } else {
            publish("没有可取消的任务，先点「launch 长任务」")
        }
    }

    /** withContext 切换 Dispatcher，并对比耗时 */
    fun runDispatchers() {
        lines.clear()
        append("== Dispatcher：Main / Default / IO ==")
        launch {
            append("当前(viewModelScope≈Main)：${threadName()}")
            val mainMs = measureTimeMillis {
                withContext(Dispatchers.Main) {
                    append("withContext(Main)：${threadName()}")
                    delay(50)
                }
            }
            val defaultMs = measureTimeMillis {
                withContext(Dispatchers.Default) {
                    append("withContext(Default)：${threadName()}")
                    // 模拟 CPU 密集
                    var acc = 0
                    repeat(200_000) { acc += it }
                    append("Default 计算 checksum=${acc % 997}")
                }
            }
            val ioMs = measureTimeMillis {
                withContext(Dispatchers.IO) {
                    append("withContext(IO)：${threadName()}")
                    delay(120) // 模拟阻塞 IO
                }
            }
            append("耗时 Main≈${mainMs}ms Default≈${defaultMs}ms IO≈${ioMs}ms")
            publish()
        }
        publish()
    }

    /** async / await 并行 */
    fun runAsyncAwait() {
        lines.clear()
        append("== async / await 并行 ==")
        launch {
            val total = measureTimeMillis {
                val a = async(Dispatchers.IO) {
                    append("async-A 开始")
                    delay(500)
                    append("async-A 结束")
                    10
                }
                val b = async(Dispatchers.IO) {
                    append("async-B 开始")
                    delay(500)
                    append("async-B 结束")
                    32
                }
                val sum = a.await() + b.await()
                append("await 结果 sum=$sum（并行约 500ms，非 1000ms）")
            }
            append("总耗时 ${total}ms")
            publish()
        }
        publish()
    }

    /** withTimeout / withTimeoutOrNull */
    fun runTimeout() {
        lines.clear()
        append("== withTimeout / withTimeoutOrNull ==")
        launch {
            try {
                withTimeout(400) {
                    append("withTimeout(400) 内执行 delay(800)…")
                    delay(800)
                    append("不应看到这行")
                }
            } catch (t: TimeoutCancellationException) {
                append("withTimeout 超时 → TimeoutCancellationException")
            }
            val result = withTimeoutOrNull(400) {
                append("withTimeoutOrNull(400) 内 delay(800)…")
                delay(800)
                "ok"
            }
            append("withTimeoutOrNull 返回: $result（超时为 null，不抛）")
            publish()
        }
        publish()
    }

    /** yield 让出执行权 */
    fun runYield() {
        lines.clear()
        append("== yield：同线程协作让出 ==")
        launch {
            launch {
                repeat(3) {
                    append("child-1 tick $it")
                    yield()
                }
            }
            launch {
                repeat(3) {
                    append("child-2 tick $it")
                    yield()
                }
            }
            delay(50)
            append("两个子协程经 yield 交错执行")
            publish()
        }
        publish()
    }

    private fun append(line: String) {
        lines += "${DateFormats.formatTimeMillis()}  $line"
    }

    private fun publish(extra: String? = null) {
        if (extra != null) append(extra)
        _uiState.value = CoroutineUiState(lines.joinToString("\n"))
    }

    private fun threadName(): String = Thread.currentThread().name
}
