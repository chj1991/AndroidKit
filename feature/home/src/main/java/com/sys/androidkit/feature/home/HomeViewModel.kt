package com.sys.androidkit.feature.home

import com.sys.androidkit.core.common.model.DemoCategory
import com.sys.androidkit.core.common.model.DemoItem
import com.sys.androidkit.core.datastore.AppPreferences
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed class HomeListItem {
    data class Category(val data: DemoCategory) : HomeListItem()
    data class Demo(val data: DemoItem) : HomeListItem()
}

data class HomeUiState(
    val query: String = "",
    val items: List<HomeListItem> = DemoCatalog.categories.map { HomeListItem.Category(it) },
    val isEmpty: Boolean = false,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val prefs: AppPreferences,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onQueryChanged(query: String) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            _uiState.value = HomeUiState(
                query = query,
                items = DemoCatalog.categories.map { HomeListItem.Category(it) },
                isEmpty = false,
            )
            return
        }
        val keyword = trimmed.lowercase()
        val demos = DemoCatalog.categories
            .flatMap { it.demos }
            .filter { demo ->
                demo.title.lowercase().contains(keyword) ||
                    demo.summary.lowercase().contains(keyword) ||
                    demo.tags.any { it.lowercase().contains(keyword) }
            }
        _uiState.update {
            HomeUiState(
                query = query,
                items = demos.map { HomeListItem.Demo(it) },
                isEmpty = demos.isEmpty(),
            )
        }
    }

    fun recordRecent(demoId: String) {
        launch { prefs.recordRecent(demoId) }
    }
}
