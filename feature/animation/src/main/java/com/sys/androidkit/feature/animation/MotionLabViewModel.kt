package com.sys.androidkit.feature.animation

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MotionUiState(
    val atEnd: Boolean = false,
    val status: String = "点击色块或按钮，在 ConstraintSet start/end 间过渡",
)

@HiltViewModel
class MotionLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(MotionUiState())
    val uiState: StateFlow<MotionUiState> = _uiState.asStateFlow()

    fun onProgress(atEnd: Boolean) {
        _uiState.update {
            it.copy(
                atEnd = atEnd,
                status = if (atEnd) "当前：end（放大靠右下）" else "当前：start（左上小卡片）",
            )
        }
    }
}
