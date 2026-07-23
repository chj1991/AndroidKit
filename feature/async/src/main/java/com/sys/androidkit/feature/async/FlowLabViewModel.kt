package com.sys.androidkit.feature.async

import androidx.lifecycle.viewModelScope
import com.sys.androidkit.core.common.time.DateFormats
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FlowUiState(
    val log: String = "选择下方按钮对比 Cold / Hot / State / Shared / 操作符",
    val stateCounter: Int = 0,
    val sharedCount: Int = 0,
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class FlowLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(FlowUiState())
    val uiState: StateFlow<FlowUiState> = _uiState.asStateFlow()

    private val lines = mutableListOf<String>()

    private val _stateCounter = MutableStateFlow(0)

    private val _events = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 8,
    )
    private val events = _events.asSharedFlow()

    private val _replayEvents = MutableSharedFlow<String>(replay = 1, extraBufferCapacity = 8)

    private var coldJobs: List<Job> = emptyList()
    private var operatorJob: Job? = null
    private var hotCollectJob: Job? = null

    init {
        launch {
            events.collect { event ->
                append("SharedFlow(replay=0) 收到: $event")
                _uiState.update { it.copy(sharedCount = it.sharedCount + 1) }
                publish()
            }
        }
    }

    fun clearLog() {
        lines.clear()
        publish("日志已清空")
    }

    /** Cold Flow：每次 collect 独立跑上游 */
    fun runColdFlow() {
        cancelCold()
        lines.clear()
        append("== Cold Flow：两次独立 collect ==")
        val cold = flow {
            append("上游 flow{} 开始（每个收集者各跑一遍）")
            repeat(5) { i ->
                delay(250)
                append("上游 emit $i")
                emit(i)
            }
            append("上游结束")
        }
        coldJobs = listOf(
            launch {
                cold.collect { v -> append("收集者-A 收到 $v") }
                append("收集者-A 完成")
                publish()
            },
            launch {
                delay(80)
                cold.collect { v -> append("收集者-B 收到 $v") }
                append("收集者-B 完成（Cold：各自一份完整序列）")
                publish()
            },
        )
        publish()
    }

    /** Hot：shareIn 上游只执行一次 */
    fun runHotShareIn() {
        hotCollectJob?.cancel()
        lines.clear()
        append("== Hot Flow：shareIn（上游只执行一次） ==")
        val hot = flow {
            append("shareIn 上游开始（整段日志里应只出现一次）")
            repeat(5) { i ->
                delay(250)
                append("上游 emit $i")
                emit(i)
            }
            append("上游结束")
        }.shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            replay = 0,
        )
        hotCollectJob = launch {
            val j1 = launch {
                hot.collect { v -> append("订阅者-A 收到 $v") }
            }
            delay(400)
            val j2 = launch {
                hot.collect { v -> append("订阅者-B 收到 $v（replay=0 可能错过前期）") }
            }
            delay(2_000)
            j1.cancel()
            j2.cancel()
            append("结论：Hot 共享上游；晚订阅会丢早期事件（除非提高 replay）")
            publish()
        }
        publish()
    }

    fun bumpStateFlow() {
        val next = _stateCounter.value + 1
        _stateCounter.value = next
        append("StateFlow.value = $next（粘性最新值）")
        _stateCounter.value = next
        append("再次 set 相同 $next → 合流，收集端不再回调")
        _uiState.update { it.copy(stateCounter = next) }
        publish()
    }

    fun emitSharedEvent() {
        val event = "event@${System.currentTimeMillis() % 100_000}"
        val ok = _events.tryEmit(event)
        append("SharedFlow.tryEmit($event) → $ok")
        publish()
    }

    fun demoReplaySharedFlow() {
        lines.clear()
        append("== SharedFlow replay=1 ==")
        launch {
            _replayEvents.emit("cached-hello")
            append("已 emit 到 replay 缓存: cached-hello")
            delay(100)
            _replayEvents.take(1).collect { v ->
                append("新订阅者立刻收到 replay: $v")
            }
            publish()
        }
        publish()
    }

    fun runOperators() {
        operatorJob?.cancel()
        lines.clear()
        append("== 操作符：debounce / filter / flatMapLatest / combine ==")
        val query = MutableStateFlow("")
        operatorJob = launch {
            val searchJob = launch {
                query
                    .debounce(300)
                    .filter { it.length >= 2 }
                    .distinctUntilChanged()
                    .flatMapLatest { q ->
                        flow {
                            append("flatMapLatest 搜索 \"$q\" …")
                            delay(400)
                            emit("result-for-$q")
                        }
                    }
                    .flowOn(Dispatchers.Default)
                    .onEach { append("收集到 $it") }
                    .collect { }
            }
            query.value = "k"
            delay(100)
            query.value = "ko"
            delay(100)
            query.value = "kot"
            delay(100)
            query.value = "kotlin"
            delay(900)
            searchJob.cancel()

            append("== combine 两路 Flow ==")
            val a = MutableStateFlow(1)
            val b = MutableStateFlow(10)
            val combined = combine(a, b) { x, y -> "a=$x + b=$y → ${x + y}" }
                .stateIn(viewModelScope, SharingStarted.Eagerly, "init")
            append("combine 初值: ${combined.value}")
            a.value = 3
            delay(50)
            append("改 a=3 → ${combined.value}")
            b.value = 7
            delay(50)
            append("改 b=7 → ${combined.value}")
            publish()
        }
        publish()
    }

    fun runCollectLatest() {
        lines.clear()
        append("== collect vs collectLatest ==")
        launch {
            val source = flow {
                emit(1)
                delay(100)
                emit(2)
                delay(100)
                emit(3)
            }
            append("-- collect：每个值处理完再收下个 --")
            source.collect { v ->
                append("collect 开始 $v")
                delay(350)
                append("collect 完成 $v")
            }
            append("-- collectLatest：新值取消未完成块 --")
            source.collectLatest { v ->
                append("collectLatest 开始 $v")
                delay(350)
                append("collectLatest 完成 $v（通常只看到最后一次完成）")
            }
            publish()
        }
        publish()
    }

    private fun cancelCold() {
        coldJobs.forEach { it.cancel() }
        coldJobs = emptyList()
    }

    private fun append(line: String) {
        synchronized(lines) {
            lines += "${DateFormats.formatTimeMillis()}  $line"
        }
    }

    private fun publish(extra: String? = null) {
        if (extra != null) append(extra)
        val text = synchronized(lines) { lines.joinToString("\n") }
        _uiState.update {
            it.copy(
                log = text,
                stateCounter = _stateCounter.value,
            )
        }
    }
}
