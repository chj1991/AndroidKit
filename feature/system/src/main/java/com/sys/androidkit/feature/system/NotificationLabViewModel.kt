package com.sys.androidkit.feature.system

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class NotificationUiState(
    val permissionGranted: Boolean = false,
    val channelsReady: Boolean = false,
    val message: String = "先创建渠道，再发送通知",
)

@HiltViewModel
class NotificationLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    fun updatePermission(granted: Boolean) {
        _uiState.update {
            it.copy(
                permissionGranted = granted,
                message = if (granted) "通知权限已授予" else "通知权限未授予（API 33+ 需先申请）",
            )
        }
    }

    fun onChannelsEnsured() {
        _uiState.update {
            it.copy(channelsReady = true, message = "已创建 3 个通知渠道")
        }
    }

    fun onPosted(channelName: String) {
        _uiState.update { it.copy(message = "已发送：$channelName") }
    }

    fun onError(message: String) {
        _uiState.update { it.copy(message = message) }
    }
}
