package com.sys.androidkit.feature.system

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class PermissionStatus {
    GRANTED,
    DENIED,
    NOT_REQUIRED,
}

data class PermissionUiState(
    val camera: PermissionStatus = PermissionStatus.DENIED,
    val notification: PermissionStatus = PermissionStatus.DENIED,
    val lastMessage: String = "",
)

@HiltViewModel
class PermissionLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(PermissionUiState())
    val uiState: StateFlow<PermissionUiState> = _uiState.asStateFlow()

    fun updateCamera(status: PermissionStatus) {
        _uiState.update {
            it.copy(
                camera = status,
                lastMessage = "相机权限结果：${status.name}",
            )
        }
    }

    fun updateNotification(status: PermissionStatus) {
        _uiState.update {
            it.copy(
                notification = status,
                lastMessage = "通知权限结果：${status.name}",
            )
        }
    }

    fun refresh(
        cameraGranted: Boolean,
        notificationStatus: PermissionStatus,
    ) {
        _uiState.update {
            it.copy(
                camera = if (cameraGranted) PermissionStatus.GRANTED else PermissionStatus.DENIED,
                notification = notificationStatus,
            )
        }
    }
}
