package com.sys.androidkit.core.network

import com.sys.androidkit.core.common.log.AppLog
import okhttp3.logging.HttpLoggingInterceptor

/**
 * 创建带敏感头脱敏的 HttpLoggingInterceptor。
 * Debug：BODY；Release：BASIC（仅方法/URL/状态，不含 body）。
 */
object NetworkLogging {

    private val redactedHeaders = setOf(
        HeaderInterceptor.HEADER_DEMO_TOKEN,
        "Authorization",
        "Cookie",
        "Set-Cookie",
    )

    fun createInterceptor(debug: Boolean): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor { message ->
            AppLog.d(message, tag = "OkHttp")
        }.apply {
            level = if (debug) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.BASIC
            }
        }
        redactedHeaders.forEach { interceptor.redactHeader(it) }
        return interceptor
    }
}
