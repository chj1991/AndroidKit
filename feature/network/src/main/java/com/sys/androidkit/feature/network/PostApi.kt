package com.sys.androidkit.feature.network

import com.squareup.moshi.JsonClass
import retrofit2.http.GET

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
}
