package com.example.kgraduate.Posts

import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PostService {
    @FormUrlEncoded
    @POST("/api/post")
    fun registerPost()

    @FormUrlEncoded
    @POST("/api/post")
    fun getPost()
}