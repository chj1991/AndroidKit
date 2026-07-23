package com.sys.androidkit.feature.network

import com.squareup.moshi.JsonClass
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

@JsonClass(generateAdapter = false)
data class PostDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String,
)

interface PostApi {
    @GET("posts")
    suspend fun getPosts(): List<PostDto>

    /** 不存在的 id → HTTP 404，供错误映射演示。 */
    @GET("posts/{id}")
    suspend fun getPost(@Path("id") id: Int): PostDto
}

/** httpbin：超时 / 5xx 场景。 */
interface HttpBinApi {
    @GET("delay/{seconds}")
    suspend fun delay(@Path("seconds") seconds: Int): ResponseBody

    @GET("status/{code}")
    suspend fun status(@Path("code") code: Int): ResponseBody
}
