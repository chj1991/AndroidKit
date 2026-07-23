package com.sys.androidkit.core.common.log

import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 进程内环形日志缓冲，供应用内 Log Viewer 订阅。
 * 由 [InMemoryLogTree] 写入。
 */
object LogBuffer {

    private const val MAX_ENTRIES = 400

    private val seq = AtomicLong(0L)
    private val _entries = MutableStateFlow<List<LogEntry>>(emptyList())
    val entries: StateFlow<List<LogEntry>> = _entries.asStateFlow()

    fun append(priority: Int, tag: String?, message: String) {
        val entry = LogEntry(
            id = seq.incrementAndGet(),
            timeMs = System.currentTimeMillis(),
            priority = priority,
            tag = tag?.takeIf { it.isNotBlank() } ?: "AndroidKit",
            message = message,
        )
        val current = _entries.value
        _entries.value = if (current.size < MAX_ENTRIES) {
            current + entry
        } else {
            current.drop(current.size - MAX_ENTRIES + 1) + entry
        }
    }

    fun clear() {
        _entries.value = emptyList()
    }

    fun snapshotText(minPriority: Int = android.util.Log.VERBOSE): String {
        return _entries.value
            .filter { it.priority >= minPriority }
            .joinToString("\n") { it.formatLine() }
    }
}
