package com.sys.androidkit.feature.charts

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ChartLabUiState(
    val type: ChartType = ChartType.LINE,
    val seed: Int = 1,
    val animateToken: Int = 0,
)

@HiltViewModel
class ChartLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(ChartLabUiState())
    val uiState: StateFlow<ChartLabUiState> = _uiState.asStateFlow()

    fun setType(type: ChartType) {
        _uiState.update { it.copy(type = type) }
    }

    fun refreshData() {
        _uiState.update { it.copy(seed = Random.nextInt(1, 10_000)) }
    }

    fun replayAnimation() {
        _uiState.update { it.copy(animateToken = it.animateToken + 1) }
    }
}
