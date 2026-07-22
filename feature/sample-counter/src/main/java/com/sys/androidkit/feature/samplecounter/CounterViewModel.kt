package com.sys.androidkit.feature.samplecounter

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CounterUiState(
    val count: Int = 0,
)

@HiltViewModel
class CounterViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    fun onPlus() {
        _uiState.update { it.copy(count = it.count + 1) }
    }

    fun onMinus() {
        _uiState.update { it.copy(count = it.count - 1) }
    }

    fun onReset() {
        _uiState.value = CounterUiState()
    }
}
