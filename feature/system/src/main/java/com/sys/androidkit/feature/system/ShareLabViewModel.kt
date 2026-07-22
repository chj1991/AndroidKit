package com.sys.androidkit.feature.system

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ShareUiState(
    val fileReady: Boolean = false,
    val message: String = "先创建缓存文件，再通过 FileProvider 分享",
    val filePath: String? = null,
)

@HiltViewModel
class ShareLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(ShareUiState())
    val uiState: StateFlow<ShareUiState> = _uiState.asStateFlow()

    fun onFileCreated(path: String) {
        _uiState.value = ShareUiState(
            fileReady = true,
            message = "已创建分享文件",
            filePath = path,
        )
    }

    fun onShared() {
        _uiState.value = _uiState.value.copy(message = "已拉起系统分享面板")
    }

    fun onError(message: String) {
        _uiState.value = _uiState.value.copy(message = message)
    }
}
