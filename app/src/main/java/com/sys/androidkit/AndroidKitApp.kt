package com.sys.androidkit

import android.app.Application
import android.os.SystemClock
import androidx.appcompat.app.AppCompatDelegate
import com.sys.androidkit.core.common.log.AppLog
import com.sys.androidkit.core.common.log.InMemoryLogTree
import com.sys.androidkit.core.common.startup.StartupTrace
import com.sys.androidkit.core.datastore.AppPreferences
import com.sys.androidkit.core.datastore.ThemeMode
import com.sys.androidkit.feature.storage.mmkv.MmkvInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltAndroidApp
class AndroidKitApp : Application() {

    @Inject
    lateinit var appPreferences: AppPreferences

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onCreate() {
        val start = SystemClock.elapsedRealtime()
        super.onCreate()
        plantTimber()
        AppLog.enabled = true
        installLeakCanaryIfDebug()
        val mmkvRoot = MmkvInitializer.init(this)
        AppLog.i("AndroidKitApp started, mmkvRoot=$mmkvRoot")
        observeTheme()
        val cost = SystemClock.elapsedRealtime() - start
        StartupTrace.markAppCreate(
            costMs = cost,
            processStartElapsedRealtime = start,
        )
        AppLog.i("Application.onCreate cost=${cost}ms")
    }

    /**
     * Debug：Logcat（DebugTree）+ 应用内缓冲（InMemoryLogTree）。
     * Release：仅 InMemoryLogTree，供 Log Viewer Demo；正式产品可改为不上报或远端 Tree。
     */
    private fun plantTimber() {
        if (Timber.forest().isNotEmpty()) return
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.plant(InMemoryLogTree())
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

    /**
     * LeakCanary 仅 debugImplementation，main 源码用反射调用 debug 源集安装器，
     * 避免 release 编译期引用 LeakCanary 类型。
     */
    private fun installLeakCanaryIfDebug() {
        if (!BuildConfig.DEBUG) return
        runCatching {
            Class.forName("com.sys.androidkit.debug.LeakCanaryInstaller")
                .getMethod("install")
                .invoke(null)
            AppLog.i("LeakCanary configured (retainedVisibleThreshold=1)")
        }.onFailure {
            AppLog.e("LeakCanaryInstaller not available", it)
        }
    }
}
