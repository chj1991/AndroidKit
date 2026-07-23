package com.sys.androidkit.core.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * NET-01 / NET-02：共享 OkHttp + Retrofit 封装。
 * - 超时（connect / read / write）
 * - HeaderInterceptor + 脱敏日志 + EventListener 监控
 * - Moshi Converter
 */
object NetworkModule {
    const val JSON_PLACEHOLDER_BASE_URL = "https://jsonplaceholder.typicode.com/"
    const val HTTPBIN_BASE_URL = "https://httpbin.org/"

    private const val DEFAULT_TIMEOUT_SEC = 15L

    val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    val okHttpClient: OkHttpClient by lazy {
        newClientBuilder().build()
    }

    /** 短超时客户端，供超时错误演示。 */
    val shortTimeoutClient: OkHttpClient by lazy {
        newClientBuilder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(2, TimeUnit.SECONDS)
            .writeTimeout(2, TimeUnit.SECONDS)
            .build()
    }

    val retrofit: Retrofit by lazy {
        newRetrofit(JSON_PLACEHOLDER_BASE_URL, okHttpClient)
    }

    /**
     * @param includeDefaultHeaders 是否挂默认 [HeaderInterceptor]
     * @param includeLogging 是否挂脱敏日志（应在 Header 之后，以便打出注入后的头）
     */
    fun newClientBuilder(
        debugLogging: Boolean = BuildConfig.DEBUG,
        includeDefaultHeaders: Boolean = true,
        includeLogging: Boolean = true,
    ): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS)
            .eventListenerFactory(NetworkTimingEventListener.FACTORY)
        if (includeDefaultHeaders) {
            builder.addInterceptor(HeaderInterceptor())
        }
        if (includeLogging) {
            builder.addInterceptor(NetworkLogging.createInterceptor(debugLogging))
        }
        return builder
    }

    fun newRetrofit(baseUrl: String, client: OkHttpClient = okHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    inline fun <reified T> createApi(
        baseUrl: String = JSON_PLACEHOLDER_BASE_URL,
        client: OkHttpClient = okHttpClient,
    ): T = newRetrofit(baseUrl, client).create(T::class.java)
}
