package com.sys.androidkit.feature.async

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FlowUiState(
    val ticks: Int = 0,
    val lastEvent: String = "尚无事件",
)

@HiltViewModel
class FlowLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(FlowUiState())
    val uiState: StateFlow<FlowUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events: SharedFlow<String> = _events.asSharedFlow()

    init {
        launch {
            _events.collect { event ->
                _uiState.update {
                    it.copy(
                        ticks = it.ticks + 1,
                        lastEvent = event,
                    )
                }
            }
        }
    }

    fun emitEvent() {
        _events.tryEmit("event@${System.currentTimeMillis() % 100000}")
    }
}
