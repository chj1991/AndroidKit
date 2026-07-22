package com.sys.androidkit.feature.animation

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AnimationUiState(
    val valueLabel: Int = 0,
    val lastAction: String = "选择一种动画",
)

@HiltViewModel
class AnimationLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(AnimationUiState())
    val uiState: StateFlow<AnimationUiState> = _uiState.asStateFlow()

    fun onAction(action: String) {
        _uiState.value = _uiState.value.copy(lastAction = action)
    }

    fun onValue(value: Int) {
        _uiState.value = _uiState.value.copy(valueLabel = value)
    }
}
