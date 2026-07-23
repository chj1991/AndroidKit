package com.sys.androidkit.core.network

/**
 * NET-03：统一网络错误模型（HTTP / 超时 / 连通性 / 解析）。
 */
sealed class NetworkError(
    override val message: String,
    override val cause: Throwable? = null,
) : Exception(message, cause) {

    data class Http(
        val code: Int,
        val bodyPreview: String? = null,
        override val message: String = "HTTP $code",
        override val cause: Throwable? = null,
    ) : NetworkError(message, cause)

    data class Timeout(
        override val message: String = "请求超时",
        override val cause: Throwable? = null,
    ) : NetworkError(message, cause)

    data class Connectivity(
        override val message: String = "网络不可用或连接失败",
        override val cause: Throwable? = null,
    ) : NetworkError(message, cause)

    data class Parse(
        override val message: String = "数据解析失败",
        override val cause: Throwable? = null,
    ) : NetworkError(message, cause)

    data class Unknown(
        override val message: String = "未知网络错误",
        override val cause: Throwable? = null,
    ) : NetworkError(message, cause)

    /** UI 友好文案，带错误类型前缀便于 Demo 对照。 */
    fun toUserMessage(): String = when (this) {
        is Http -> "HTTP($code): $message"
        is Timeout -> "TIMEOUT: $message"
        is Connectivity -> "CONNECTIVITY: $message"
        is Parse -> "PARSE: $message"
        is Unknown -> "UNKNOWN: $message"
    }

    fun typeLabel(): String = when (this) {
        is Http -> "Http($code)"
        is Timeout -> "Timeout"
        is Connectivity -> "Connectivity"
        is Parse -> "Parse"
        is Unknown -> "Unknown"
    }
}
