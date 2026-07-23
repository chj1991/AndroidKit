package com.sys.androidkit.feature.network

import android.content.Context
import com.sys.androidkit.core.common.time.DateFormats
import com.sys.androidkit.core.network.HeaderInterceptor
import com.sys.androidkit.core.ui.base.BaseViewModel
import com.sys.androidkit.core.ui.widget.DemoState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request

enum class CacheMode {
    DEFAULT,
    FORCE_NETWORK,
    FORCE_CACHE,
}

data class CacheUiState(
    val demoState: DemoState = DemoState.Content,
    val mode: CacheMode = CacheMode.DEFAULT,
    val report: String = "",
    val cacheStats: String = "",
)

private data class CacheFetchResult(
    val code: Int,
    val fromCacheOnly: Boolean,
    val hasNetwork: Boolean,
    val hasCache: Boolean,
    val cacheControl: String?,
    val bodyPreview: String,
)

@HiltViewModel
class CacheLabViewModel @Inject constructor(
    @ApplicationContext context: Context,
) : BaseViewModel() {

    private val cacheDir = File(context.cacheDir, "okhttp_http_cache")
    private val cache = Cache(cacheDir, CACHE_SIZE_BYTES)

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(HeaderInterceptor(clientName = "AndroidKit-CacheLab"))
            .addNetworkInterceptor { chain ->
                val response = chain.proceed(chain.request())
                response.newBuilder()
                    .header("Cache-Control", "public, max-age=60")
                    .removeHeader("Pragma")
                    .build()
            }
            .build()
    }

    private val _uiState = MutableStateFlow(
        CacheUiState(cacheStats = cacheStatsText()),
    )
    val uiState: StateFlow<CacheUiState> = _uiState.asStateFlow()

    fun fetch(mode: CacheMode) {
        launch {
            _uiState.value = _uiState.value.copy(
                demoState = DemoState.Loading,
                mode = mode,
            )
            runCatching {
                withContext(Dispatchers.IO) {
                    val cacheControl = when (mode) {
                        CacheMode.DEFAULT -> CacheControl.Builder().build()
                        CacheMode.FORCE_NETWORK -> CacheControl.FORCE_NETWORK
                        CacheMode.FORCE_CACHE -> CacheControl.FORCE_CACHE
                    }
                    val request = Request.Builder()
                        .url(DEMO_URL)
                        .cacheControl(cacheControl)
                        .get()
                        .build()
                    client.newCall(request).execute().use { response ->
                        CacheFetchResult(
                            code = response.code,
                            fromCacheOnly = response.cacheResponse != null &&
                                response.networkResponse == null,
                            hasNetwork = response.networkResponse != null,
                            hasCache = response.cacheResponse != null,
                            cacheControl = response.header("Cache-Control"),
                            bodyPreview = response.body?.string().orEmpty().take(120),
                        )
                    }
                }
            }.onSuccess { result ->
                val report = buildString {
                    appendLine("mode=$mode @ ${DateFormats.formatTimeMillis()}")
                    appendLine("HTTP ${result.code} · fromCache=${result.fromCacheOnly}")
                    appendLine("networkResponse=${result.hasNetwork}")
                    appendLine("cacheResponse=${result.hasCache}")
                    appendLine("Cache-Control: ${result.cacheControl}")
                    appendLine()
                    appendLine(result.bodyPreview)
                }
                _uiState.value = CacheUiState(
                    demoState = DemoState.Content,
                    mode = mode,
                    report = report,
                    cacheStats = cacheStatsText(),
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    demoState = DemoState.Error(
                        message = error.message ?: "请求失败（FORCE_CACHE 且无缓存时常见）",
                    ),
                    report = "",
                    cacheStats = cacheStatsText(),
                )
            }
        }
    }

    fun clearCache() {
        launch {
            withContext(Dispatchers.IO) { cache.evictAll() }
            _uiState.value = _uiState.value.copy(
                cacheStats = cacheStatsText(),
                report = "已清空 OkHttp Cache",
                demoState = DemoState.Content,
            )
        }
    }

    private fun cacheStatsText(): String {
        return "cache hits=${cache.hitCount()} network=${cache.networkCount()} " +
            "req=${cache.requestCount()} size=${cache.size()}/${cache.maxSize()}"
    }

    companion object {
        private const val CACHE_SIZE_BYTES = 5L * 1024 * 1024
        private const val DEMO_URL = "https://jsonplaceholder.typicode.com/posts/1"
    }
}
