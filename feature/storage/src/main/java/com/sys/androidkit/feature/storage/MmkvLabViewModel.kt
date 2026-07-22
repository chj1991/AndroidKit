package com.sys.androidkit.feature.storage

import com.sys.androidkit.core.ui.base.BaseViewModel
import com.sys.androidkit.feature.storage.mmkv.MmkvStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MmkvUiState(
    val nickname: String = "",
    val counter: Int = 0,
    val flag: Boolean = false,
    val keys: List<String> = emptyList(),
    val totalSize: Long = 0,
    val status: String = "MMKV 同步读写，改完即落盘（无需 apply）",
)

@HiltViewModel
class MmkvLabViewModel @Inject constructor(
    private val store: MmkvStore,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(MmkvUiState())
    val uiState: StateFlow<MmkvUiState> = _uiState.asStateFlow()

    init {
        refresh("已从 MMKV 读取当前值")
    }

    fun saveNickname(value: String) {
        val text = value.ifBlank { "androidkit" }
        store.putString(MmkvStore.KEY_NICKNAME, text)
        refresh("已写入 nickname=$text")
    }

    fun incCounter() {
        val next = store.getInt(MmkvStore.KEY_COUNTER) + 1
        store.putInt(MmkvStore.KEY_COUNTER, next)
        refresh("counter → $next")
    }

    fun toggleFlag() {
        val next = !store.getBoolean(MmkvStore.KEY_FLAG)
        store.putBoolean(MmkvStore.KEY_FLAG, next)
        refresh("flag → $next")
    }

    fun clearDemoKeys() {
        store.remove(MmkvStore.KEY_NICKNAME)
        store.remove(MmkvStore.KEY_COUNTER)
        store.remove(MmkvStore.KEY_FLAG)
        refresh("已清除 Demo 三个 key")
    }

    private fun refresh(status: String) {
        _uiState.update {
            MmkvUiState(
                nickname = store.getString(MmkvStore.KEY_NICKNAME),
                counter = store.getInt(MmkvStore.KEY_COUNTER),
                flag = store.getBoolean(MmkvStore.KEY_FLAG),
                keys = store.allKeys(),
                totalSize = store.totalSize(),
                status = status,
            )
        }
    }
}
