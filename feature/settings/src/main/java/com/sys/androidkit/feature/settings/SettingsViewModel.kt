package com.sys.androidkit.feature.settings

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

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: AppPreferences,
) : BaseViewModel() {

    val uiState: StateFlow<SettingsUiState> = prefs.themeMode
        .map { name ->
            SettingsUiState(
                themeMode = runCatching { ThemeMode.valueOf(name) }.getOrDefault(ThemeMode.SYSTEM),
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    fun setTheme(mode: ThemeMode) {
        launch { prefs.setThemeMode(mode) }
    }
}
