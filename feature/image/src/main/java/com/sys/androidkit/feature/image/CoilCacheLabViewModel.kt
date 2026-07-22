package com.sys.androidkit.feature.image

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class CoilCacheMode {
    DEFAULT,
    MEMORY_ONLY,
    DISK_ONLY,
    DISABLED,
}

data class CoilCacheUiState(
    val mode: CoilCacheMode = CoilCacheMode.DEFAULT,
    val imageUrl: String = SAMPLE_URL,
    val loadToken: Int = 0,
    val status: String = "选择缓存策略后点击加载；二次加载观察是否走缓存",
) {
    companion object {
        const val SAMPLE_URL =
            "https://picsum.photos/seed/androidkit-cache/640/360"
    }
}

@HiltViewModel
class CoilCacheLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(CoilCacheUiState())
    val uiState: StateFlow<CoilCacheUiState> = _uiState.asStateFlow()

    fun setMode(mode: CoilCacheMode) {
        _uiState.update {
            it.copy(
                mode = mode,
                status = "策略已切换为 ${mode.name}，点击加载生效",
            )
        }
    }

    fun reload() {
        _uiState.update {
            it.copy(
                loadToken = it.loadToken + 1,
                status = "开始加载（mode=${it.mode}）…",
            )
        }
    }

    fun onLoadFinished(fromMemory: Boolean?, message: String) {
        _uiState.update {
            it.copy(
                status = buildString {
                    append(message)
                    if (fromMemory != null) {
                        append(if (fromMemory) "\n来源提示：可能命中内存缓存" else "\n来源提示：非内存缓存（磁盘/网络）")
                    }
                },
            )
        }
    }
}
