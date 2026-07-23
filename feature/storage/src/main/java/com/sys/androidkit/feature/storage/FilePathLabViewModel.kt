package com.sys.androidkit.feature.storage

import android.content.Context
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

data class FilePathUiState(
    val report: String = "",
    val status: String = "",
)

@HiltViewModel
class FilePathLabViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(FilePathUiState())
    val uiState: StateFlow<FilePathUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        launch {
            val report = withContext(Dispatchers.IO) { buildReport() }
            _uiState.value = _uiState.value.copy(report = report)
        }
    }

    fun writeFilesSample() {
        launch {
            val file = File(context.filesDir, SAMPLE_FILES)
            withContext(Dispatchers.IO) {
                file.writeText("filesDir sample @ ${System.currentTimeMillis()}\n")
            }
            _uiState.value = _uiState.value.copy(
                status = "已写入 filesDir：\n${file.absolutePath}",
            )
            refresh()
        }
    }

    fun writeCacheSample() {
        launch {
            val file = File(context.cacheDir, SAMPLE_CACHE)
            withContext(Dispatchers.IO) {
                file.writeText("cacheDir sample @ ${System.currentTimeMillis()}\n")
            }
            _uiState.value = _uiState.value.copy(
                status = "已写入 cacheDir：\n${file.absolutePath}",
            )
            refresh()
        }
    }

    fun clearCacheSample() {
        launch {
            val deleted = withContext(Dispatchers.IO) {
                File(context.cacheDir, SAMPLE_CACHE).delete()
            }
            _uiState.value = _uiState.value.copy(
                status = if (deleted) "已删除 cache 样例文件" else "cache 样例不存在或删除失败",
            )
            refresh()
        }
    }

    private fun buildReport(): String {
        val filesDir = context.filesDir
        val cacheDir = context.cacheDir
        val externalFiles = context.getExternalFilesDir(null)
        val externalCache = context.externalCacheDir
        return buildString {
            appendLine("内部持久 filesDir")
            appendLine("  ${filesDir.absolutePath}")
            appendLine("  exists=${filesDir.exists()} size=${dirSize(filesDir)} bytes")
            appendLine("  sample=${File(filesDir, SAMPLE_FILES).let { if (it.exists()) it.name else "—" }}")
            appendLine()
            appendLine("内部缓存 cacheDir（系统可清）")
            appendLine("  ${cacheDir.absolutePath}")
            appendLine("  exists=${cacheDir.exists()} size=${dirSize(cacheDir)} bytes")
            appendLine("  sample=${File(cacheDir, SAMPLE_CACHE).let { if (it.exists()) it.name else "—" }}")
            appendLine()
            appendLine("外部应用专属 getExternalFilesDir")
            appendLine("  ${externalFiles?.absolutePath ?: "null"}")
            appendLine()
            appendLine("外部缓存 externalCacheDir")
            appendLine("  ${externalCache?.absolutePath ?: "null"}")
            appendLine()
            appendLine("要点：")
            appendLine("• filesDir：卸载才清，适合小型私有文件")
            appendLine("• cacheDir：可被系统随时清理，勿存唯一数据")
            appendLine("• 外部专属目录：用户可见于 Android/data，仍属应用私有")
            appendLine("• 共享媒体请走 MediaStore / SAF（见 Scoped Storage Lab）")
        }
    }

    private fun dirSize(dir: File): Long {
        if (!dir.exists()) return 0
        return dir.walkTopDown().filter { it.isFile }.map { it.length() }.sum()
    }

    companion object {
        private const val SAMPLE_FILES = "androidkit_files_demo.txt"
        private const val SAMPLE_CACHE = "androidkit_cache_demo.txt"
    }
}
