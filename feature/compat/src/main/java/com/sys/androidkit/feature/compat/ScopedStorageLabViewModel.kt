package com.sys.androidkit.feature.compat

import android.content.Context
import android.os.Build
import android.os.Environment
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ScopedStorageUiState(
    val report: String = "",
    val status: String = "应用专属目录写入通常无需存储权限",
)

@HiltViewModel
class ScopedStorageLabViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ScopedStorageUiState())
    val uiState: StateFlow<ScopedStorageUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val externalApp = context.getExternalFilesDir(null)
        val externalCache = context.externalCacheDir
        val legacyExternal = Environment.getExternalStorageDirectory()
        val report = buildString {
            appendLine("SDK=${Build.VERSION.SDK_INT} / ${Build.VERSION.RELEASE}")
            appendLine("isExternalStorageLegacy=${Environment.isExternalStorageLegacy()}")
            appendLine()
            appendLine("filesDir:\n  ${context.filesDir}")
            appendLine("cacheDir:\n  ${context.cacheDir}")
            appendLine("getExternalFilesDir:\n  $externalApp")
            appendLine("externalCacheDir:\n  $externalCache")
            appendLine("Environment.getExternalStorageDirectory (旧路径，受限):\n  $legacyExternal")
            appendLine()
            appendLine("要点：")
            appendLine("• 应用专属目录（files/cache/Android/data）无需 READ/WRITE")
            appendLine("• 共享媒体走 MediaStore / Photo Picker / SAF")
            appendLine("• Android 10+ 默认 Scoped Storage；requestLegacyExternalStorage 仅过渡")
        }
        _uiState.update { it.copy(report = report) }
    }

    fun writeAppSpecificFile() {
        launch {
            val dir = context.getExternalFilesDir(null) ?: context.filesDir
            val file = File(dir, "scoped_demo.txt")
            file.writeText("AndroidKit Scoped Storage demo @ ${System.currentTimeMillis()}\n")
            _uiState.update {
                it.copy(status = "已写入（无需存储权限）：\n${file.absolutePath}")
            }
            refresh()
        }
    }
}
