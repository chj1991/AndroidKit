package com.sys.androidkit.feature.network

import com.sys.androidkit.core.common.result.AppResult
import com.sys.androidkit.core.network.NetworkError
import com.sys.androidkit.core.network.NetworkModule
import com.sys.androidkit.core.network.NetworkMonitor
import com.sys.androidkit.core.network.safeApiCall
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class RetrofitScenario {
    LIST,
    NOT_FOUND,
    SERVER_ERROR,
    TIMEOUT,
}

data class RetrofitUiState(
    val result: AppResult<List<PostDto>> = AppResult.Loading,
    val isRefreshing: Boolean = false,
    val refreshError: String? = null,
    val scenario: RetrofitScenario = RetrofitScenario.LIST,
    val errorType: String? = null,
    val metricsText: String = "监控：等待请求（EventListener → DNS/连接/TTFB）",
)

@HiltViewModel
class RetrofitLabViewModel @Inject constructor(
    private val api: PostApi,
) : BaseViewModel() {

    private val httpBinApi: HttpBinApi by lazy {
        NetworkModule.createApi(NetworkModule.HTTPBIN_BASE_URL)
    }

    private val timeoutHttpBinApi: HttpBinApi by lazy {
        NetworkModule.createApi(
            baseUrl = NetworkModule.HTTPBIN_BASE_URL,
            client = NetworkModule.shortTimeoutClient,
        )
    }

    private val _uiState = MutableStateFlow(RetrofitUiState())
    val uiState: StateFlow<RetrofitUiState> = _uiState.asStateFlow()

    init {
        refresh(fromSwipe = false)
    }

    fun setScenario(scenario: RetrofitScenario) {
        _uiState.value = _uiState.value.copy(scenario = scenario)
    }

    fun refresh(fromSwipe: Boolean = true) {
        launch {
            when (_uiState.value.scenario) {
                RetrofitScenario.LIST -> loadList(fromSwipe)
                RetrofitScenario.NOT_FOUND -> loadErrorScenario { api.getPost(NOT_FOUND_ID) }
                RetrofitScenario.SERVER_ERROR -> loadErrorScenario {
                    httpBinApi.status(500).close()
                }
                RetrofitScenario.TIMEOUT -> loadErrorScenario {
                    timeoutHttpBinApi.delay(10).close()
                }
            }
        }
    }

    private suspend fun loadList(fromSwipe: Boolean) {
        val previous = _uiState.value.result
        val keepList = fromSwipe && previous is AppResult.Success
        _uiState.value = if (keepList) {
            _uiState.value.copy(
                isRefreshing = true,
                refreshError = null,
                errorType = null,
            )
        } else {
            _uiState.value.copy(
                result = AppResult.Loading,
                isRefreshing = false,
                refreshError = null,
                errorType = null,
            )
        }

        when (val outcome = safeApiCall { api.getPosts().take(20) }) {
            is AppResult.Success -> {
                _uiState.value = _uiState.value.copy(
                    result = outcome,
                    isRefreshing = false,
                    refreshError = null,
                    errorType = null,
                    metricsText = metricsLine(),
                )
            }
            is AppResult.Error -> {
                val type = (outcome.cause as? NetworkError)?.typeLabel()
                _uiState.value = if (keepList) {
                    _uiState.value.copy(
                        result = previous,
                        isRefreshing = false,
                        refreshError = outcome.message,
                        errorType = type,
                        metricsText = metricsLine(),
                    )
                } else {
                    _uiState.value.copy(
                        result = outcome,
                        isRefreshing = false,
                        refreshError = null,
                        errorType = type,
                        metricsText = metricsLine(),
                    )
                }
            }
            AppResult.Loading -> Unit
        }
    }

    private suspend fun loadErrorScenario(block: suspend () -> Unit) {
        _uiState.value = _uiState.value.copy(
            result = AppResult.Loading,
            isRefreshing = false,
            refreshError = null,
            errorType = null,
        )
        when (val outcome = safeApiCall { block(); emptyList<PostDto>() }) {
            is AppResult.Success -> {
                _uiState.value = _uiState.value.copy(
                    result = AppResult.Error("预期应失败，却成功了"),
                    metricsText = metricsLine(),
                )
            }
            is AppResult.Error -> {
                val type = (outcome.cause as? NetworkError)?.typeLabel()
                _uiState.value = _uiState.value.copy(
                    result = outcome,
                    errorType = type,
                    metricsText = metricsLine(),
                )
            }
            AppResult.Loading -> Unit
        }
    }

    private fun metricsLine(): String {
        val last = NetworkMonitor.last
        return if (last != null) {
            "监控：${last.summary()}"
        } else {
            "监控：暂无 EventListener 数据"
        }
    }

    companion object {
        private const val NOT_FOUND_ID = 999_999
    }
}
