package com.sys.androidkit.feature.network

import android.content.Context
import com.sys.androidkit.core.common.time.DateFormats
import com.sys.androidkit.core.network.HeaderInterceptor
import com.sys.androidkit.core.network.ProgressRequestBody
import com.sys.androidkit.core.network.ProgressResponseBody
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okio.buffer
import okio.sink

data class ProgressUiState(
    val title: String = "空闲",
    val percent: Int = 0,
    val indeterminate: Boolean = false,
    val detail: String = "选择下载或上传开始演示",
    val running: Boolean = false,
)

@HiltViewModel
class ProgressLabViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    private val activeJob = AtomicReference<Job?>(null)

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HeaderInterceptor(clientName = "AndroidKit-ProgressLab"))
            .build()
    }

    fun download() {
        start("下载") {
            val request = Request.Builder().url(DOWNLOAD_URL).get().build()
            withContext(Dispatchers.IO) {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        error("HTTP ${response.code}")
                    }
                    val rawBody = response.body ?: error("empty body")
                    val progressBody = ProgressResponseBody(rawBody) { bytes, total, done ->
                        publishProgress(
                            title = "下载中",
                            bytes = bytes,
                            total = total,
                            done = done,
                        )
                    }
                    val outFile = File(context.cacheDir, "progress_download.bin")
                    progressBody.source().use { source ->
                        outFile.sink().buffer().use { sink ->
                            sink.writeAll(source)
                        }
                    }
                    "已保存 ${outFile.length()} bytes → ${outFile.name}"
                }
            }
        }
    }

    fun upload() {
        start("上传") {
            val payload = ByteArray(UPLOAD_BYTES) { (it % 256).toByte() }
            val rawBody = payload.toRequestBody("application/octet-stream".toMediaType())
            val progressBody = ProgressRequestBody(rawBody) { bytes, total, done ->
                publishProgress(
                    title = "上传中",
                    bytes = bytes,
                    total = total,
                    done = done,
                )
            }
            val request = Request.Builder()
                .url(UPLOAD_URL)
                .post(progressBody)
                .build()
            withContext(Dispatchers.IO) {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        error("HTTP ${response.code}")
                    }
                    val preview = response.body?.string().orEmpty().take(80)
                    "上传完成 HTTP ${response.code} · $preview"
                }
            }
        }
    }

    fun cancel() {
        activeJob.getAndSet(null)?.cancel()
        _uiState.value = ProgressUiState(
            title = "已取消",
            percent = _uiState.value.percent,
            detail = "任务已取消 @ ${DateFormats.formatTimeMillis()}",
            running = false,
        )
    }

    private fun start(label: String, block: suspend () -> String) {
        activeJob.get()?.cancel()
        _uiState.value = ProgressUiState(
            title = "$label…",
            percent = 0,
            indeterminate = true,
            detail = "连接中…",
            running = true,
        )
        val job = launch {
            runCatching { block() }
                .onSuccess { message ->
                    if (isActive) {
                        _uiState.value = ProgressUiState(
                            title = "$label 完成",
                            percent = 100,
                            detail = "$message\n@ ${DateFormats.formatTimeMillis()}",
                            running = false,
                        )
                    }
                }
                .onFailure { error ->
                    if (isActive) {
                        _uiState.value = ProgressUiState(
                            title = "$label 失败",
                            percent = _uiState.value.percent,
                            detail = error.message ?: "未知错误",
                            running = false,
                        )
                    }
                }
        }
        activeJob.set(job)
        job.invokeOnCompletion { activeJob.compareAndSet(job, null) }
    }

    private fun publishProgress(title: String, bytes: Long, total: Long, done: Boolean) {
        val percent = when {
            total > 0 -> ((bytes * 100) / total).toInt().coerceIn(0, 100)
            done -> 100
            else -> _uiState.value.percent
        }
        _uiState.value = ProgressUiState(
            title = if (done) "$title（收尾）" else title,
            percent = percent,
            indeterminate = total <= 0 && !done,
            detail = if (total > 0) {
                "${formatBytes(bytes)} / ${formatBytes(total)} ($percent%)"
            } else {
                "${formatBytes(bytes)} / ?"
            },
            running = !done,
        )
    }

    private fun formatBytes(value: Long): String {
        if (value < 1024) return "$value B"
        if (value < 1024 * 1024) return "%.1f KB".format(value / 1024.0)
        return "%.2f MB".format(value / (1024.0 * 1024.0))
    }

    companion object {
        private const val DOWNLOAD_URL = "https://httpbin.org/bytes/524288"
        private const val UPLOAD_URL = "https://httpbin.org/post"
        private const val UPLOAD_BYTES = 256 * 1024
    }
}
