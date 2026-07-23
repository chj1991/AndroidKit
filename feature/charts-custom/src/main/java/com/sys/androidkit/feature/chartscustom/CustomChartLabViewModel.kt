package com.sys.androidkit.feature.chartscustom

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CustomChartUiState(
    val type: CustomChartType = CustomChartType.LINE,
    val seed: Int = 1,
    val animateToken: Int = 0,
)

@HiltViewModel
class CustomChartLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(CustomChartUiState())
    val uiState: StateFlow<CustomChartUiState> = _uiState.asStateFlow()

    fun setType(type: CustomChartType) {
        _uiState.update { it.copy(type = type) }
    }

    fun refreshData() {
        _uiState.update { it.copy(seed = Random.nextInt(1, 10_000)) }
    }

    fun replayAnimation() {
        _uiState.update { it.copy(animateToken = it.animateToken + 1) }
    }
}
