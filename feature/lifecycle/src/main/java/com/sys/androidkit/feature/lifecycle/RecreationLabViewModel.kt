package com.sys.androidkit.feature.lifecycle

import androidx.lifecycle.SavedStateHandle
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RecreationUiState(
    val vmSessionId: String,
    val vmCounter: Int = 0,
    val savedCounter: Int = 0,
    val logs: List<String> = emptyList(),
)

@HiltViewModel
class RecreationLabViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    /** 仅存活于 ViewModel：旋转保留，进程死亡丢失 */
    val vmSessionId: String = UUID.randomUUID().toString().take(8)

    private val _uiState = MutableStateFlow(
        RecreationUiState(
            vmSessionId = vmSessionId,
            savedCounter = savedStateHandle[KEY_SAVED_COUNTER] ?: 0,
        ),
    )
    val uiState: StateFlow<RecreationUiState> = _uiState.asStateFlow()

    init {
        append("ViewModel.init session=$vmSessionId saved=${_uiState.value.savedCounter}")
    }

    fun incrementVm() {
        _uiState.update { it.copy(vmCounter = it.vmCounter + 1) }
        append("VM counter → ${_uiState.value.vmCounter}")
    }

    fun incrementSaved() {
        val next = _uiState.value.savedCounter + 1
        savedStateHandle[KEY_SAVED_COUNTER] = next
        _uiState.update { it.copy(savedCounter = next) }
        append("SavedStateHandle counter → $next")
    }

    fun append(event: String) {
        _uiState.update { it.copy(logs = it.logs + event) }
    }

    fun clearLogs() {
        _uiState.update { it.copy(logs = emptyList()) }
    }

    companion object {
        private const val KEY_SAVED_COUNTER = "saved_counter"
    }
}
