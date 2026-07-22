package com.sys.androidkit.feature.image

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ImageUiState(
    val status: String = "点击按钮加载图片",
    val imageUrl: String? = null,
    val forceError: Boolean = false,
)

@HiltViewModel
class ImageLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(ImageUiState())
    val uiState: StateFlow<ImageUiState> = _uiState.asStateFlow()

    fun loadSuccess() {
        // picsum 随机图，带时间戳避免完全命中同一缓存键时看不出变化
        val url = "https://picsum.photos/seed/androidkit/800/500"
        _uiState.value = ImageUiState(
            status = "加载成功图…",
            imageUrl = url,
            forceError = false,
        )
    }

    fun loadFail() {
        _uiState.value = ImageUiState(
            status = "加载失败图…",
            imageUrl = "https://invalid.androidkit.local/not-found.png",
            forceError = true,
        )
    }

    fun clear() {
        _uiState.value = ImageUiState(status = "已清空", imageUrl = null, forceError = false)
    }

    fun onLoadResult(success: Boolean, message: String) {
        _uiState.value = _uiState.value.copy(status = message)
    }
}
