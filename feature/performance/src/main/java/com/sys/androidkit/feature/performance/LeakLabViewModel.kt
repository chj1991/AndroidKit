package com.sys.androidkit.feature.performance

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LeakUiState(
    val description: String = LeakyActivityHolder.describe(),
    val leaking: Boolean = LeakyActivityHolder.isLeakingActivity(),
)

@HiltViewModel
class LeakLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(LeakUiState())
    val uiState: StateFlow<LeakUiState> = _uiState.asStateFlow()

    fun refresh() {
        _uiState.value = LeakUiState(
            description = LeakyActivityHolder.describe(),
            leaking = LeakyActivityHolder.isLeakingActivity(),
        )
    }
}
