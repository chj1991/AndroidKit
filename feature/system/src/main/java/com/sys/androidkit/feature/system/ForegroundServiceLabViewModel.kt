package com.sys.androidkit.feature.system

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope

data class ForegroundServiceUiState(
    val running: Boolean = false,
    val seconds: Int = 0,
)

@HiltViewModel
class ForegroundServiceLabViewModel @Inject constructor() : BaseViewModel() {

    val uiState: StateFlow<ForegroundServiceUiState> = combine(
        ForegroundTimerBus.running,
        ForegroundTimerBus.seconds,
    ) { running, seconds ->
        ForegroundServiceUiState(running = running, seconds = seconds)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ForegroundServiceUiState())
}
