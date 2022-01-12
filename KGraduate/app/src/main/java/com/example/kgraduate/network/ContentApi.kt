package com.example.kgraduate.network

import com.example.kgraduate.repository.dto.response.ImageResponse
import com.example.kgraduate.repository.dto.response.PostResponse
import com.example.kgraduate.repository.entity.Post
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ContentApi {
    @FormUrlEncoded
    @POST("/api/post")
    fun registerPost1(
        /*@Header("Bearer") token : String,*/
        @Field("type") type: String,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("creator_name") creator_name: String,
        @Field("creator_id") creator_id: String,
        @Field("file_id") file_id: String
    ): Call<PostResponse>

    @Multipart
    @POST("/api/uploadImg")
    fun uploadImg1(
        /*@Header("Bearer") token : String,*/
        @Part img: MultipartBody.Part
    ): Call<ImageResponse>

    @GET("/api/post")
    fun getPost1(
        @Header("Bearer")
        token: String
    ): Call<Post>
}