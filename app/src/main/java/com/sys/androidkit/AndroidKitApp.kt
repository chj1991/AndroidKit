package com.sys.androidkit

import android.app.Application
import android.os.SystemClock
import androidx.appcompat.app.AppCompatDelegate
import com.sys.androidkit.core.common.log.AppLog
import com.sys.androidkit.core.common.startup.StartupTrace
import com.sys.androidkit.core.datastore.AppPreferences
import com.sys.androidkit.core.datastore.ThemeMode
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@HiltAndroidApp
class AndroidKitApp : Application() {

    @Inject
    lateinit var appPreferences: AppPreferences

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onCreate() {
        val start = SystemClock.elapsedRealtime()
        super.onCreate()
        AppLog.enabled = BuildConfig.DEBUG
        AppLog.i("AndroidKitApp started")
        observeTheme()
        val cost = SystemClock.elapsedRealtime() - start
        StartupTrace.markAppCreate(
            costMs = cost,
            processStartElapsedRealtime = start,
        )
        AppLog.i("Application.onCreate cost=${cost}ms")
    }

    private fun observeTheme() {
        appScope.launch {
            appPreferences.themeMode
                .distinctUntilChanged()
                .collect { modeName ->
                    val mode = runCatching { ThemeMode.valueOf(modeName) }.getOrDefault(ThemeMode.SYSTEM)
                    AppCompatDelegate.setDefaultNightMode(
                        when (mode) {
                            ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                        },
                    )
                }
        }
    }
}
