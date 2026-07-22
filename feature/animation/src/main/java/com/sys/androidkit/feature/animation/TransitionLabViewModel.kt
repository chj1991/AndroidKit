package com.sys.androidkit.feature.animation

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TransitionUiState(
    val expanded: Boolean = false,
    val sceneB: Boolean = false,
    val lastAction: String = "使用 TransitionManager 做布局变更动画",
)

@HiltViewModel
class TransitionLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(TransitionUiState())
    val uiState: StateFlow<TransitionUiState> = _uiState.asStateFlow()

    fun toggleExpand() {
        _uiState.update {
            val next = !it.expanded
            it.copy(
                expanded = next,
                lastAction = if (next) "ChangeBounds + Fade：展开详情" else "ChangeBounds + Fade：收起详情",
            )
        }
    }

    fun toggleScene() {
        _uiState.update {
            val next = !it.sceneB
            it.copy(
                sceneB = next,
                lastAction = if (next) "AutoTransition：切换到 Scene B" else "AutoTransition：切回 Scene A",
            )
        }
    }
}
