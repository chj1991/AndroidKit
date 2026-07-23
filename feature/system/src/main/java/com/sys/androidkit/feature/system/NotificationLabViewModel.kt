package com.sys.androidkit.feature.system

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive

data class NotificationUiState(
    val permissionGranted: Boolean = false,
    val channelsReady: Boolean = false,
    val progressRunning: Boolean = false,
    val message: String = "先创建渠道，再体验各类通知样式",
)

sealed interface ProgressTick {
    data class Running(val percent: Int, val indeterminate: Boolean) : ProgressTick
    data object Done : ProgressTick
}

@HiltViewModel
class NotificationLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    private val _progressTicks = MutableSharedFlow<ProgressTick>(extraBufferCapacity = 8)
    val progressTicks: SharedFlow<ProgressTick> = _progressTicks.asSharedFlow()

    private var progressJob: Job? = null

    fun updatePermission(granted: Boolean) {
        _uiState.update { state ->
            state.copy(
                permissionGranted = granted,
                message = when {
                    !granted -> "通知权限未授予（API 33+ 需先在 Permission Lab 申请）"
                    state.message.contains("未授予") -> "通知权限已授予，可体验各类样式"
                    else -> state.message
                },
            )
        }
    }

    fun onChannelsEnsured() {
        _uiState.update {
            it.copy(channelsReady = true, message = "已创建进度 / 大图 / 气泡等渠道")
        }
    }

    fun onPosted(label: String) {
        _uiState.update { it.copy(message = "已发送：$label") }
    }

    fun onError(message: String) {
        _uiState.update { it.copy(message = message) }
    }

    fun startProgressDemo() {
        if (_uiState.value.progressRunning) return
        progressJob?.cancel()
        progressJob = launch {
            _uiState.update { it.copy(progressRunning = true, message = "进度通知演示中…") }
            _progressTicks.emit(ProgressTick.Running(percent = 0, indeterminate = true))
            delay(400)
            var percent = 0
            while (isActive && percent < 100) {
                percent = (percent + 5).coerceAtMost(100)
                _progressTicks.emit(ProgressTick.Running(percent = percent, indeterminate = false))
                delay(180)
            }
            if (isActive) {
                _progressTicks.emit(ProgressTick.Done)
                _uiState.update {
                    it.copy(progressRunning = false, message = "已发送：进度通知（完成）")
                }
            }
        }
    }

    fun cancelProgressDemo() {
        progressJob?.cancel()
        progressJob = null
        _uiState.update { it.copy(progressRunning = false, message = "进度演示已取消") }
    }

    override fun onCleared() {
        progressJob?.cancel()
        super.onCleared()
    }
}
