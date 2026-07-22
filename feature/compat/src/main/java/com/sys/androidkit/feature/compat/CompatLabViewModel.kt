package com.sys.androidkit.feature.compat

import android.os.Build
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CompatUiState(
    val sdkInt: Int = Build.VERSION.SDK_INT,
    val release: String = Build.VERSION.RELEASE.orEmpty(),
    val changes: List<CompatChange> = CompatCatalog.changes,
)

@HiltViewModel
class CompatLabViewModel @Inject constructor() : BaseViewModel() {
    val uiState: StateFlow<CompatUiState> = MutableStateFlow(CompatUiState()).asStateFlow()
}
