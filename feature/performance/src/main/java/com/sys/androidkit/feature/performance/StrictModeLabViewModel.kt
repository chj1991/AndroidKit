package com.sys.androidkit.feature.performance

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class StrictModeUiState(
    val enabled: Boolean = false,
    val message: String = "开启 StrictMode 后点击下方按钮，观察 Logcat / 屏幕闪红",
)

@HiltViewModel
class StrictModeLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(StrictModeUiState())
    val uiState: StateFlow<StrictModeUiState> = _uiState.asStateFlow()

    fun setEnabled(enabled: Boolean) {
        _uiState.value = StrictModeUiState(
            enabled = enabled,
            message = if (enabled) "StrictMode 已启用" else "StrictMode 已关闭",
        )
    }

    fun setMessage(message: String) {
        _uiState.value = _uiState.value.copy(message = message)
    }
}
