package com.sys.androidkit.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "android_kit_prefs")

class AppPreferences(private val context: Context) {

    private val themeKey = stringPreferencesKey("theme_mode")
    private val favoritesKey = stringSetPreferencesKey("favorite_demo_ids")
    private val recentKey = stringPreferencesKey("recent_demo_ids")
    private val biometricLoginKey = booleanPreferencesKey("biometric_login_enabled")

    val themeMode: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[themeKey] ?: ThemeMode.SYSTEM.name
    }

    /** SYS-06：是否允许用生物识别解锁演示账号会话 */
    val biometricLoginEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[biometricLoginKey] ?: false
    }

    val favoriteDemoIds: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[favoritesKey].orEmpty()
    }

    /** 最近浏览 demoId，最新在前，最多 [MAX_RECENT] 条，逗号分隔存储 */
    val recentDemoIds: Flow<List<String>> = context.dataStore.data.map { prefs ->
        prefs[recentKey].orEmpty()
            .split(',')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { prefs ->
            prefs[themeKey] = mode.name
        }
    }

    suspend fun toggleFavorite(demoId: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[favoritesKey].orEmpty().toMutableSet()
            if (!current.add(demoId)) {
                current.remove(demoId)
            }
            prefs[favoritesKey] = current
        }
    }

    suspend fun recordRecent(demoId: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[recentKey].orEmpty()
                .split(',')
                .map { it.trim() }
                .filter { it.isNotEmpty() && it != demoId }
                .toMutableList()
            current.add(0, demoId)
            prefs[recentKey] = current.take(MAX_RECENT).joinToString(",")
        }
    }

    suspend fun setBiometricLoginEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[biometricLoginKey] = enabled
        }
    }

    companion object {
        private const val MAX_RECENT = 20
    }
}

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
}
