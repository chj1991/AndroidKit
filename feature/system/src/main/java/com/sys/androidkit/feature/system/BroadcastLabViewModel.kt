package com.sys.androidkit.feature.system

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class BroadcastUiState(
    val registered: Boolean = false,
    val logs: List<String> = emptyList(),
)

@HiltViewModel
class BroadcastLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(BroadcastUiState())
    val uiState: StateFlow<BroadcastUiState> = _uiState.asStateFlow()

    private val formatter = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    fun setRegistered(registered: Boolean) {
        _uiState.update {
            it.copy(
                registered = registered,
                logs = it.logs + stamp(if (registered) "动态注册 Receiver" else "取消注册 Receiver"),
            )
        }
    }

    fun onReceived(payload: String) {
        _uiState.update {
            it.copy(logs = it.logs + stamp("收到广播：$payload"))
        }
    }

    fun onSent(payload: String) {
        _uiState.update {
            it.copy(logs = it.logs + stamp("已发送：$payload"))
        }
    }

    private fun stamp(message: String): String = "${formatter.format(Date())}  $message"
}
