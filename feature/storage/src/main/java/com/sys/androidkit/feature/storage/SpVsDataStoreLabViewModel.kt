package com.sys.androidkit.feature.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewModelScope
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

private val Context.compareDataStore by preferencesDataStore(name = "sp_vs_datastore_demo")

data class SpVsDataStoreUiState(
    val spValue: String = "",
    val dataStoreValue: String = "",
    val status: String = "SP 同步读写；DataStore 异步 + Flow 观察",
)

@HiltViewModel
class SpVsDataStoreLabViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : BaseViewModel() {

    private val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    private val dsKey = stringPreferencesKey(KEY)

    private val dataStoreFlow = context.compareDataStore.data.map { prefs ->
        prefs[dsKey].orEmpty()
    }

    val uiState: StateFlow<SpVsDataStoreUiState> = dataStoreFlow
        .map { dsValue ->
            SpVsDataStoreUiState(
                spValue = sp.getString(KEY, "").orEmpty(),
                dataStoreValue = dsValue,
                status = "左侧 SP / 右侧 DataStore；写入后对比读法差异",
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            SpVsDataStoreUiState(spValue = sp.getString(KEY, "").orEmpty()),
        )

    fun writeBoth(value: String) {
        val text = value.ifBlank { "androidkit-${System.currentTimeMillis() % 10000}" }
        sp.edit().putString(KEY, text).apply()
        launch {
            context.compareDataStore.edit { it[dsKey] = text }
        }
    }

    fun clearBoth() {
        sp.edit().remove(KEY).apply()
        launch {
            context.compareDataStore.edit { it.remove(dsKey) }
        }
    }

    companion object {
        private const val SP_NAME = "sp_vs_datastore_demo_sp"
        private const val KEY = "nickname"
    }
}
