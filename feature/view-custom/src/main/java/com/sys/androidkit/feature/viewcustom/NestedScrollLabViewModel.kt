package com.sys.androidkit.feature.viewcustom

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class NestedScrollUiState(
    val childWins: Boolean = true,
    val status: String = "子布局优先：横向滑动更顺畅",
)

@HiltViewModel
class NestedScrollLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(NestedScrollUiState())
    val uiState: StateFlow<NestedScrollUiState> = _uiState.asStateFlow()

    fun setChildWins(enabled: Boolean) {
        _uiState.update {
            it.copy(
                childWins = enabled,
                status = if (enabled) {
                    "子布局优先：横向 ScrollView 会 requestDisallowIntercept"
                } else {
                    "父布局优先：竖向 ScrollView 更容易抢走手势"
                },
            )
        }
    }
}
