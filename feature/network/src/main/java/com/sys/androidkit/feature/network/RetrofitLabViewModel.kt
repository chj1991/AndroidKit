package com.sys.androidkit.feature.network

import com.sys.androidkit.core.common.result.AppResult
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RetrofitUiState(
    val result: AppResult<List<PostDto>> = AppResult.Loading,
    val isRefreshing: Boolean = false,
    val refreshError: String? = null,
)

@HiltViewModel
class RetrofitLabViewModel @Inject constructor(
    private val api: PostApi,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(RetrofitUiState())
    val uiState: StateFlow<RetrofitUiState> = _uiState.asStateFlow()

    init {
        refresh(fromSwipe = false)
    }

    fun refresh(fromSwipe: Boolean = true) {
        launch {
            val previous = _uiState.value.result
            val keepList = fromSwipe && previous is AppResult.Success
            _uiState.value = if (keepList) {
                _uiState.value.copy(isRefreshing = true, refreshError = null)
            } else {
                RetrofitUiState(result = AppResult.Loading)
            }
            runCatching { api.getPosts().take(20) }
                .onSuccess { posts ->
                    _uiState.value = RetrofitUiState(
                        result = AppResult.Success(posts),
                        isRefreshing = false,
                    )
                }
                .onFailure { error ->
                    val message = error.message ?: "网络请求失败"
                    _uiState.value = if (keepList) {
                        RetrofitUiState(
                            result = previous,
                            isRefreshing = false,
                            refreshError = message,
                        )
                    } else {
                        RetrofitUiState(
                            result = AppResult.Error(message, error),
                            isRefreshing = false,
                        )
                    }
                }
        }
    }
}
