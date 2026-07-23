package com.sys.androidkit.core.network

import com.sys.androidkit.core.common.result.AppResult
import kotlinx.coroutines.CancellationException

/**
 * 统一包装挂起 API：成功 → [AppResult.Success]，失败 → 映射为 [NetworkError] 的 [AppResult.Error]。
 * [CancellationException] 原样抛出，避免吞掉协程取消。
 */
suspend fun <T> safeApiCall(block: suspend () -> T): AppResult<T> {
    return try {
        AppResult.Success(block())
    } catch (canceled: CancellationException) {
        throw canceled
    } catch (t: Throwable) {
        val error = NetworkErrorMapper.map(t)
        AppResult.Error(error.toUserMessage(), error)
    }
}
