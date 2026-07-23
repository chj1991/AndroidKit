package com.sys.androidkit.feature.settings

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AboutUiState(
    val appName: String = "",
    val packageName: String = "",
    val versionName: String = "",
    val versionCode: Long = 0,
    val modulesText: String = "",
)

@HiltViewModel
class AboutViewModel @Inject constructor(
    @ApplicationContext context: Context,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(load(context))
    val uiState: StateFlow<AboutUiState> = _uiState.asStateFlow()

    private fun load(context: Context): AboutUiState {
        val pm = context.packageManager
        val packageName = context.packageName
        val info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            pm.getPackageInfo(packageName, 0)
        }
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            info.versionCode.toLong()
        }
        val appName = info.applicationInfo?.loadLabel(pm)?.toString()
            ?: context.getString(R.string.feature_settings_about_fallback_name)
        return AboutUiState(
            appName = appName,
            packageName = packageName,
            versionName = info.versionName.orEmpty().ifEmpty { "—" },
            versionCode = versionCode,
            modulesText = MODULES,
        )
    }

    companion object {
        private val MODULES = """
            架构：View + XML + ViewBinding · MVVM · Hilt · Navigation
            core：common / ui / datastore / database / network
            feature：home · settings · lifecycle · async · recycler · storage
            · network · system · view-custom · animation · image
            · performance · compat · sample-counter
        """.trimIndent()
    }
}
