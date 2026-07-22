package com.sys.androidkit.feature.animation

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LottieUiState(
    val playing: Boolean = true,
    val speed: Float = 1f,
    val status: String = "Lottie 播放本地 JSON（assets）",
)

@HiltViewModel
class LottieLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(LottieUiState())
    val uiState: StateFlow<LottieUiState> = _uiState.asStateFlow()

    fun togglePlay() {
        _uiState.update {
            val next = !it.playing
            it.copy(
                playing = next,
                status = if (next) "播放中" else "已暂停",
            )
        }
    }

    fun cycleSpeed() {
        _uiState.update {
            val next = when {
                it.speed < 1f -> 1f
                it.speed < 1.5f -> 1.5f
                it.speed < 2f -> 2f
                else -> 0.5f
            }
            it.copy(speed = next, status = "速度 x$next")
        }
    }
}
