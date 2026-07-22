package com.sys.androidkit.feature.async

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

data class CoroutineUiState(
    val status: String = "空闲",
)

@HiltViewModel
class CoroutineLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(CoroutineUiState())
    val uiState: StateFlow<CoroutineUiState> = _uiState.asStateFlow()

    private var job: Job? = null

    fun start() {
        job?.cancel()
        job = launch {
            _uiState.value = CoroutineUiState("运行中（IO）…")
            val result = withContext(Dispatchers.IO) {
                var progress = 0
                while (isActive && progress < 5) {
                    delay(400)
                    progress++
                }
                "完成，步进 $progress"
            }
            _uiState.value = CoroutineUiState(result)
        }
    }

    fun cancel() {
        job?.cancel()
        _uiState.value = CoroutineUiState("已取消")
    }
}
