package com.sys.androidkit.core.common.log

import android.util.Log
import com.sys.androidkit.core.common.time.DateFormats

data class LogEntry(
    val id: Long,
    val timeMs: Long,
    val priority: Int,
    val tag: String,
    val message: String,
) {
    fun priorityLabel(): String = when (priority) {
        Log.VERBOSE -> "V"
        Log.DEBUG -> "D"
        Log.INFO -> "I"
        Log.WARN -> "W"
        Log.ERROR -> "E"
        Log.ASSERT -> "A"
        else -> "?"
    }

    fun formatLine(): String =
        "${DateFormats.formatTimeMillis(timeMs)} ${priorityLabel()}/$tag: $message"
}
