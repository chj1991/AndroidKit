package com.sys.androidkit.feature.lifecycle

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

data class LifecycleObserverUiState(
    val logs: List<String> = emptyList(),
)

@HiltViewModel
class LifecycleObserverLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(LifecycleObserverUiState())
    val uiState: StateFlow<LifecycleObserverUiState> = _uiState.asStateFlow()

    private val formatter = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    fun append(event: String) {
        val line = "${formatter.format(Date())}  $event"
        _uiState.update { it.copy(logs = it.logs + line) }
    }

    fun clear() {
        _uiState.value = LifecycleObserverUiState()
    }
}
