package com.sys.androidkit.feature.performance

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.sys.androidkit.core.common.log.AppLog
import com.sys.androidkit.core.common.log.LogBuffer
import com.sys.androidkit.core.common.log.LogEntry
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

data class LogViewerUiState(
    val logs: List<LogEntry> = emptyList(),
    val minPriority: Int = Log.VERBOSE,
    val totalCount: Int = 0,
)

@HiltViewModel
class LogViewerLabViewModel @Inject constructor() : BaseViewModel() {

    private val minPriority = MutableStateFlow(Log.VERBOSE)

    val uiState: StateFlow<LogViewerUiState> = combine(
        LogBuffer.entries,
        minPriority,
    ) { entries, min ->
        LogViewerUiState(
            logs = entries.filter { it.priority >= min },
            minPriority = min,
            totalCount = entries.size,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LogViewerUiState(),
    )

    fun setMinPriority(priority: Int) {
        minPriority.value = priority
    }

    fun clear() {
        LogBuffer.clear()
        AppLog.i("LogBuffer cleared from Log Viewer")
    }

    fun emitSamples() {
        Timber.tag("LogViewer").v("VERBOSE sample from Timber")
        Timber.tag("LogViewer").d("DEBUG sample from Timber")
        AppLog.i("INFO sample from AppLog bridge")
        Timber.tag("LogViewer").w("WARN sample — check filter chips")
        Timber.tag("LogViewer").e(IllegalStateException("demo"), "ERROR sample with throwable")
    }

    fun exportText(): String = LogBuffer.snapshotText(minPriority.value)
}
