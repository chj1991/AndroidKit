package com.sys.androidkit.feature.androidktx

import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AndroidKtxHubUiState(
    val group: AndroidKtxGroup? = null,
    val query: String = "",
    val items: List<AndroidKtxEntry> = AndroidKtxCatalog.all,
)

@HiltViewModel
class AndroidKtxHubViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(AndroidKtxHubUiState())
    val uiState: StateFlow<AndroidKtxHubUiState> = _uiState.asStateFlow()

    fun setGroup(group: AndroidKtxGroup?) {
        _uiState.update {
            it.copy(group = group, items = AndroidKtxCatalog.filter(group, it.query))
        }
    }

    fun setQuery(query: String) {
        _uiState.update {
            it.copy(query = query, items = AndroidKtxCatalog.filter(it.group, query))
        }
    }
}
