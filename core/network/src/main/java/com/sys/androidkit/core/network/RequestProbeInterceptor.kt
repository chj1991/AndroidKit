package com.sys.androidkit.core.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 捕获最终发出的请求信息，供 Interceptor Lab UI 展示（不含敏感体）。
 */
class RequestProbeInterceptor(
    private val onProbed: (ProbedRequest) -> Unit,
) : Interceptor {

    data class ProbedRequest(
        val method: String,
        val url: String,
        val headers: Map<String, String>,
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val headers = buildMap {
            for (name in request.headers.names()) {
                put(name, request.header(name).orEmpty())
            }
        }
        onProbed(
            ProbedRequest(
                method = request.method,
                url = request.url.toString(),
                headers = headers,
            ),
        )
        return chain.proceed(request)
    }
}
