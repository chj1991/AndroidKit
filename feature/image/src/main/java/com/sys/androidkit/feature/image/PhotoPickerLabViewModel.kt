package com.sys.androidkit.feature.image

import android.net.Uri
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PhotoPickerUiState(
    val status: String = "尚未选择图片",
    val imageUri: Uri? = null,
    val pickerAvailable: Boolean? = null,
)

@HiltViewModel
class PhotoPickerLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(PhotoPickerUiState())
    val uiState: StateFlow<PhotoPickerUiState> = _uiState.asStateFlow()

    fun onAvailability(available: Boolean) {
        _uiState.update {
            it.copy(
                pickerAvailable = available,
                status = if (available) {
                    "系统 Photo Picker 可用（通常无需存储权限）"
                } else {
                    "Photo Picker 不可用，将走兼容路径（仍可能不弹权限）"
                },
            )
        }
    }

    fun onPicked(uri: Uri) {
        _uiState.update {
            it.copy(
                imageUri = uri,
                status = "已选择：\n$uri",
            )
        }
    }

    fun onCancelled() {
        _uiState.update { it.copy(status = "用户取消选择") }
    }

    fun clear() {
        _uiState.update {
            it.copy(
                imageUri = null,
                status = "已清空预览",
            )
        }
    }
}
