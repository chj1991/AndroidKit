package com.sys.androidkit.feature.recycler

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class RecyclerRow {
    data class Header(val id: String, val title: String) : RecyclerRow()
    data class Item(val id: String, val title: String) : RecyclerRow()
}

data class RecyclerUiState(
    val rows: List<RecyclerRow> = emptyList(),
    val message: String = "点击条目试试",
)

@HiltViewModel
class RecyclerLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(
        RecyclerUiState(
            rows = buildList {
                add(RecyclerRow.Header("h1", "分组 A"))
                addAll((1..5).map { RecyclerRow.Item("a$it", "条目 A-$it") })
                add(RecyclerRow.Header("h2", "分组 B"))
                addAll((1..5).map { RecyclerRow.Item("b$it", "条目 B-$it") })
            },
        ),
    )
    val uiState: StateFlow<RecyclerUiState> = _uiState.asStateFlow()

    fun shuffle() {
        val items = _uiState.value.rows.filterIsInstance<RecyclerRow.Item>().shuffled()
        _uiState.value = RecyclerUiState(
            rows = listOf(RecyclerRow.Header("h1", "打乱后")) + items,
            message = "已通过 DiffUtil 局部刷新",
        )
    }

    fun onItemClick(item: RecyclerRow.Item) {
        _uiState.value = _uiState.value.copy(message = "点击：${item.title}")
    }
}
