package com.sys.androidkit.feature.components

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ComponentHubUiState(
    val group: ComponentGroup? = null,
    val query: String = "",
    val items: List<ComponentEntry> = ComponentCatalog.all,
)

@HiltViewModel
class ComponentHubViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(ComponentHubUiState())
    val uiState: StateFlow<ComponentHubUiState> = _uiState.asStateFlow()

    fun setGroup(group: ComponentGroup?) {
        _uiState.update {
            it.copy(group = group, items = ComponentCatalog.filter(group, it.query))
        }
    }

    fun setQuery(query: String) {
        _uiState.update {
            it.copy(query = query, items = ComponentCatalog.filter(it.group, query))
        }
    }
}
