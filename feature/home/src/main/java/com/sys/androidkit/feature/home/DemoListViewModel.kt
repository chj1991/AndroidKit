package com.sys.androidkit.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sys.androidkit.core.common.model.DemoItem
import com.sys.androidkit.core.datastore.AppPreferences
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class DemoListRow(
    val demo: DemoItem,
    val favorite: Boolean,
)

data class DemoListUiState(
    val title: String = "",
    val rows: List<DemoListRow> = emptyList(),
)

@HiltViewModel
class DemoListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val prefs: AppPreferences,
) : BaseViewModel() {

    private val categoryId: String =
        savedStateHandle.get<String>(DemoListFragment.ARG_CATEGORY_ID).orEmpty()

    private val category = DemoCatalog.findCategory(categoryId)

    val uiState: StateFlow<DemoListUiState> = prefs.favoriteDemoIds
        .map { favorites ->
            DemoListUiState(
                title = category?.title.orEmpty(),
                rows = category?.demos.orEmpty().map { demo ->
                    DemoListRow(demo = demo, favorite = demo.id in favorites)
                },
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            DemoListUiState(
                title = category?.title.orEmpty(),
                rows = category?.demos.orEmpty().map { DemoListRow(it, false) },
            ),
        )

    fun toggleFavorite(demoId: String) {
        launch { prefs.toggleFavorite(demoId) }
    }

    fun recordRecent(demoId: String) {
        launch { prefs.recordRecent(demoId) }
    }
}
