package com.sys.androidkit.feature.storage

import androidx.lifecycle.viewModelScope
import com.sys.androidkit.core.datastore.AppPreferences
import com.sys.androidkit.core.datastore.ThemeMode
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class DataStoreUiState(
    val themeMode: String = ThemeMode.SYSTEM.name,
)

@HiltViewModel
class DataStoreLabViewModel @Inject constructor(
    private val prefs: AppPreferences,
) : BaseViewModel() {

    val uiState: StateFlow<DataStoreUiState> = prefs.themeMode
        .map { DataStoreUiState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DataStoreUiState())

    fun setTheme(mode: ThemeMode) {
        launch { prefs.setThemeMode(mode) }
    }
}
