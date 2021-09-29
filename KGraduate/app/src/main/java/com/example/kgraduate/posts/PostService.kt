package com.example.kgraduate.posts

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