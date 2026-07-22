package com.sys.androidkit.feature.recycler

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TouchItem(
    val id: String,
    val title: String,
)

data class TouchHelperUiState(
    val items: List<TouchItem> = emptyList(),
    val message: String = "长按拖拽排序，左滑删除；ItemDecoration 负责间距分割线",
)

@HiltViewModel
class TouchHelperLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(
        TouchHelperUiState(
            items = defaultItems(),
        ),
    )
    val uiState: StateFlow<TouchHelperUiState> = _uiState.asStateFlow()

    fun replaceAll(items: List<TouchItem>, message: String? = null) {
        _uiState.update {
            it.copy(
                items = items,
                message = message ?: "已同步顺序（共 ${items.size} 项）",
            )
        }
    }

    fun reset() {
        _uiState.value = TouchHelperUiState(
            items = defaultItems(),
            message = "已重置列表",
        )
    }

    private fun defaultItems(): List<TouchItem> =
        (1..12).map { TouchItem("t$it", "可拖拽条目 $it") }
}