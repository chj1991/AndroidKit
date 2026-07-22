package com.sys.androidkit.feature.image

import android.net.Uri
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CustomImagePickerUiState(
    val maxCount: Int = 1,
    val maxCountInput: String = "1",
    val status: String = "设置可选张数后打开选择界面；确认后返回本页展示",
    val confirmedPreview: List<Uri> = emptyList(),
) {
    val modeLabel: String
        get() = if (maxCount <= 1) "当前模式：单选（1 张）" else "当前模式：多选（最多 $maxCount 张）"
}

@HiltViewModel
class CustomImagePickerLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(CustomImagePickerUiState())
    val uiState: StateFlow<CustomImagePickerUiState> = _uiState.asStateFlow()

    fun onMaxCountInputChanged(value: String) {
        _uiState.update { it.copy(maxCountInput = value) }
    }

    fun applyMaxCount() {
        val max = resolveMaxCount()
        _uiState.update {
            it.copy(
                maxCount = max,
                maxCountInput = max.toString(),
                status = if (max == 1) "已设为单选，打开选择界面后点确认返回" else "已设为多选（最多 $max 张）",
            )
        }
    }

    fun resolveMaxCount(): Int {
        val parsed = _uiState.value.maxCountInput.trim().toIntOrNull()
        return parsed?.coerceIn(1, MAX_ALLOWED) ?: 1
    }

    fun onPickedResult(uris: List<Uri>) {
        _uiState.update {
            it.copy(
                confirmedPreview = uris,
                status = if (uris.isEmpty()) "未选择图片" else "已从选择界面返回，共 ${uris.size} 张",
            )
        }
    }

    fun clearResult() {
        _uiState.update {
            it.copy(
                confirmedPreview = emptyList(),
                status = "已清空结果",
            )
        }
    }

    companion object {
        private const val MAX_ALLOWED = 20
    }
}
