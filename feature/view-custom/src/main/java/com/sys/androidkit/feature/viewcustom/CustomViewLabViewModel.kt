package com.sys.androidkit.feature.viewcustom

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CustomViewUiState(
    val progressPercent: Int = 35,
)

@HiltViewModel
class CustomViewLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(CustomViewUiState())
    val uiState: StateFlow<CustomViewUiState> = _uiState.asStateFlow()

    fun setProgressPercent(percent: Int) {
        _uiState.value = CustomViewUiState(percent.coerceIn(0, 100))
    }
}
