package com.sys.androidkit.feature.image

import android.net.Uri
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CameraXUiState(
    val hasPermission: Boolean = false,
    val bound: Boolean = false,
    val status: String = "申请相机权限后绑定 Preview + ImageCapture",
    val capturedUri: Uri? = null,
)

@HiltViewModel
class CameraXLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(CameraXUiState())
    val uiState: StateFlow<CameraXUiState> = _uiState.asStateFlow()

    fun onPermissionChanged(granted: Boolean) {
        _uiState.update {
            it.copy(
                hasPermission = granted,
                status = if (granted) "已授权，正在绑定用例…" else "缺少 CAMERA 权限",
                bound = if (granted) it.bound else false,
            )
        }
    }

    fun onBound(success: Boolean, message: String) {
        _uiState.update {
            it.copy(bound = success, status = message)
        }
    }

    fun onCaptured(uri: Uri) {
        _uiState.update {
            it.copy(
                capturedUri = uri,
                status = "拍照成功：\n$uri",
            )
        }
    }

    fun onCaptureError(message: String) {
        _uiState.update { it.copy(status = "拍照失败：$message") }
    }

    fun clearCapture() {
        _uiState.update {
            it.copy(capturedUri = null, status = if (it.bound) "预览中，可继续拍照" else it.status)
        }
    }
}
