package com.example.kgraduate.posts

import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface PostService {
    @FormUrlEncoded
    @POST("/api/post")
    fun registerPost()


    @GET("/api/post")
    fun getPost(@Header("Bearer") token : String) : Call<Post>
}