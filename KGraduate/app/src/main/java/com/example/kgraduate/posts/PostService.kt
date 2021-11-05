package com.example.kgraduate.posts

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface PostService {
    @FormUrlEncoded
    @POST("/api/post")
    fun registerPost(/*@Header("Bearer") token : String,*/ @Field("type") type : String,
                     @Field("title") title : String, @Field("content") content : String, @Field("creator_name") creator_name : String,
                     @Field("creator_id") creator_id : String, @Field("file_id") file_id : String) : Call<PostResponse>

    @Multipart
    @POST("/api/uploadImg")
    fun uploadImg(/*@Header("Bearer") token : String,*/ @Part img : MultipartBody.Part) : Call<ImageResponse>

    @GET("/api/post")
    fun getPost(@Header("Bearer") token : String) : Call<Post>
}