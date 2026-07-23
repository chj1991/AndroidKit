package com.sys.androidkit.feature.image

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ImageStyle {
    NONE,
    ROUNDED,
    CIRCLE,
    GRAYSCALE,
    ROUNDED_GRAY,
}

enum class ImageSizeMode {
    /** 按 ImageView 尺寸采样（推荐，省内存） */
    VIEW_SIZE,

    /** 强制小尺寸解码，演示 downsampling */
    DOWNSAMPLE,

    /** 原图尺寸（可能更大内存） */
    ORIGINAL,
}

data class ImageUiState(
    val status: String = "选择样式后点击「加载成功图」",
    val imageUrl: String? = null,
    val style: ImageStyle = ImageStyle.NONE,
    val sizeMode: ImageSizeMode = ImageSizeMode.VIEW_SIZE,
    val loadToken: Int = 0,
    val forceError: Boolean = false,
)

@HiltViewModel
class ImageLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(ImageUiState())
    val uiState: StateFlow<ImageUiState> = _uiState.asStateFlow()

    fun setStyle(style: ImageStyle) {
        _uiState.value = _uiState.value.copy(style = style)
    }

    fun setSizeMode(mode: ImageSizeMode) {
        _uiState.value = _uiState.value.copy(sizeMode = mode)
    }

    fun loadSuccess() {
        // 固定 seed，便于对比不同 Transformation / Size 的缓存键差异
        _uiState.value = _uiState.value.copy(
            status = "加载中…（placeholder → crossfade）",
            imageUrl = SUCCESS_URL,
            forceError = false,
            loadToken = _uiState.value.loadToken + 1,
        )
    }

    fun loadFail() {
        _uiState.value = _uiState.value.copy(
            status = "加载失败图…（应显示 error drawable）",
            imageUrl = FAIL_URL,
            forceError = true,
            loadToken = _uiState.value.loadToken + 1,
        )
    }

    fun clear() {
        _uiState.value = ImageUiState(
            status = "已清空（可演示 fallback：无 data 时的占位）",
            style = _uiState.value.style,
            sizeMode = _uiState.value.sizeMode,
        )
    }

    fun onLoadResult(message: String) {
        _uiState.value = _uiState.value.copy(status = message)
    }

    companion object {
        private const val SUCCESS_URL = "https://picsum.photos/seed/androidkit-coil/800/500"
        private const val FAIL_URL = "https://invalid.androidkit.local/not-found.png"
    }
}
