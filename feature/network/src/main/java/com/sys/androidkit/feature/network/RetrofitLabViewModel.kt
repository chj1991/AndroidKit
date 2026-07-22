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
)

@HiltViewModel
class RetrofitLabViewModel @Inject constructor(
    private val api: PostApi,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(RetrofitUiState())
    val uiState: StateFlow<RetrofitUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        launch {
            _uiState.value = RetrofitUiState(AppResult.Loading)
            runCatching { api.getPosts().take(20) }
                .onSuccess { posts ->
                    _uiState.value = RetrofitUiState(AppResult.Success(posts))
                }
                .onFailure { error ->
                    _uiState.value = RetrofitUiState(
                        AppResult.Error(error.message ?: "网络请求失败", error),
                    )
                }
        }
    }
}
