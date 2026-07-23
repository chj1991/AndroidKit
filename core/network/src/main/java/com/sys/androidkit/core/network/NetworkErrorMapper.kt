package com.sys.androidkit.core.network

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import retrofit2.HttpException

object NetworkErrorMapper {

    fun map(throwable: Throwable): NetworkError {
        if (throwable is NetworkError) return throwable

        return when (throwable) {
            is HttpException -> {
                val preview = runCatching {
                    throwable.response()?.errorBody()?.string()?.take(200)
                }.getOrNull()
                NetworkError.Http(
                    code = throwable.code(),
                    bodyPreview = preview,
                    message = throwable.message().ifBlank { "HTTP ${throwable.code()}" },
                    cause = throwable,
                )
            }
            is SocketTimeoutException -> NetworkError.Timeout(cause = throwable)
            is UnknownHostException,
            is ConnectException,
            -> NetworkError.Connectivity(
                message = throwable.message ?: "无法连接服务器",
                cause = throwable,
            )
            is JsonDataException,
            is JsonEncodingException,
            -> NetworkError.Parse(
                message = throwable.message ?: "JSON 解析失败",
                cause = throwable,
            )
            is IOException -> {
                val msg = throwable.message.orEmpty()
                if (msg.contains("timeout", ignoreCase = true)) {
                    NetworkError.Timeout(message = msg.ifBlank { "请求超时" }, cause = throwable)
                } else {
                    NetworkError.Connectivity(
                        message = msg.ifBlank { "网络 IO 失败" },
                        cause = throwable,
                    )
                }
            }
            else -> NetworkError.Unknown(
                message = throwable.message ?: throwable.javaClass.simpleName,
                cause = throwable,
            )
        }
    }
}
