package com.sys.androidkit.feature.basicui

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class BasicUiHubUiState(
    val group: BasicUiGroup? = null,
    val query: String = "",
    val items: List<BasicUiEntry> = BasicUiCatalog.all,
)

@HiltViewModel
class BasicUiHubViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(BasicUiHubUiState())
    val uiState: StateFlow<BasicUiHubUiState> = _uiState.asStateFlow()

    fun setGroup(group: BasicUiGroup?) {
        _uiState.update {
            it.copy(group = group, items = BasicUiCatalog.filter(group, it.query))
        }
    }

    fun setQuery(query: String) {
        _uiState.update {
            it.copy(query = query, items = BasicUiCatalog.filter(it.group, query))
        }
    }
}
