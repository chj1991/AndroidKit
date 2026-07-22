package com.sys.androidkit.core.network

import okhttp3.Interceptor
import okhttp3.Response
import java.util.UUID

/**
 * NET-04：统一注入演示用请求头（Client / Request-Id / Accept）。
 */
class HeaderInterceptor(
    private val clientName: String = "AndroidKit",
    private val extraHeaders: () -> Map<String, String> = { emptyMap() },
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
            .header(HEADER_CLIENT, clientName)
            .header(HEADER_REQUEST_ID, UUID.randomUUID().toString())
            .header("Accept", "application/json")
        extraHeaders().forEach { (key, value) ->
            builder.header(key, value)
        }
        return chain.proceed(builder.build())
    }

    companion object {
        const val HEADER_CLIENT = "X-AndroidKit-Client"
        const val HEADER_REQUEST_ID = "X-Request-Id"
        const val HEADER_DEMO_TOKEN = "X-Demo-Token"
    }
}
