package com.sys.androidkit.feature.network

import com.sys.androidkit.core.network.HeaderInterceptor
import com.sys.androidkit.core.network.RequestProbeInterceptor
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

data class InterceptorUiState(
    val loading: Boolean = false,
    val token: String = "demo-token",
    val probedSummary: String = "",
    val responseSummary: String = "",
    val error: String? = null,
)

@HiltViewModel
class InterceptorLabViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(InterceptorUiState())
    val uiState: StateFlow<InterceptorUiState> = _uiState.asStateFlow()

    @Volatile
    private var lastProbe: RequestProbeInterceptor.ProbedRequest? = null

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(
                HeaderInterceptor(
                    extraHeaders = {
                        mapOf(HeaderInterceptor.HEADER_DEMO_TOKEN to _uiState.value.token)
                    },
                ),
            )
            .addInterceptor(
                RequestProbeInterceptor { probed ->
                    lastProbe = probed
                },
            )
            .build()
    }

    fun onTokenChanged(token: String) {
        _uiState.value = _uiState.value.copy(token = token)
    }

    fun fireRequest() {
        launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            runCatching {
                withContext(Dispatchers.IO) {
                    val request = Request.Builder()
                        .url("https://jsonplaceholder.typicode.com/posts/1")
                        .get()
                        .build()
                    client.newCall(request).execute().use { response ->
                        val bodyPreview = response.body?.string().orEmpty().take(160)
                        response.code to bodyPreview
                    }
                }
            }.onSuccess { (code, bodyPreview) ->
                val probe = lastProbe
                val headersText = probe?.headers
                    ?.entries
                    ?.sortedBy { it.key.lowercase() }
                    ?.joinToString("\n") { "${it.key}: ${it.value}" }
                    .orEmpty()
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    probedSummary = buildString {
                        appendLine("${probe?.method.orEmpty()} ${probe?.url.orEmpty()}")
                        appendLine()
                        append(headersText)
                    }.trim(),
                    responseSummary = "HTTP $code\n\n$bodyPreview",
                    error = null,
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = error.message ?: "请求失败",
                )
            }
        }
    }
}
