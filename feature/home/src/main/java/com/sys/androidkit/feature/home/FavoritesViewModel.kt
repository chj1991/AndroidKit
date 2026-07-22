package com.sys.androidkit.feature.home

import androidx.lifecycle.viewModelScope
import com.sys.androidkit.core.common.model.DemoItem
import com.sys.androidkit.core.datastore.AppPreferences
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class FavoritesUiState(
    val favorites: List<DemoItem> = emptyList(),
    val recent: List<DemoItem> = emptyList(),
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val prefs: AppPreferences,
) : BaseViewModel() {

    val uiState: StateFlow<FavoritesUiState> = combine(
        prefs.favoriteDemoIds,
        prefs.recentDemoIds,
    ) { favoriteIds, recentIds ->
        FavoritesUiState(
            favorites = favoriteIds.mapNotNull { DemoCatalog.findDemo(it) },
            recent = recentIds.mapNotNull { DemoCatalog.findDemo(it) },
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        FavoritesUiState(),
    )

    fun toggleFavorite(demoId: String) {
        launch { prefs.toggleFavorite(demoId) }
    }

    fun openRecorded(demoId: String) {
        launch { prefs.recordRecent(demoId) }
    }
}
