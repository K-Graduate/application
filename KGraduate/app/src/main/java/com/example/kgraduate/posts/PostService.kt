package com.example.kgraduate.posts

import com.example.kgraduate.repository.dto.response.ImageResponse
import com.example.kgraduate.repository.dto.response.PostResponse
import com.example.kgraduate.repository.dto.response.getPostResponse
import com.example.kgraduate.repository.entity.Post
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface PostService {
    @POST("/api/post")
    fun registerPost(
        /*@Header("Bearer") token : String,
        @Field("type") type: String,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("creator_name") creator_name: String,
        @Field("creator_id") creator_id: String,
        @Field("file_id") file_id: String*/
        @Body postData: JsonObject
    ): Call<PostResponse>

    @Multipart
    @POST("/api/uploadImg")
    fun uploadImg(
        /*@Header("Bearer") token : String,*/
        @Part img: MultipartBody.Part
    ): Call<ImageResponse>

    // 첫 게시물 5개 불러오기
    @GET("/api/post")
    fun getFirstPost(
        @Header("Authorization") token: String/*,
        @Field("type") type: String*/
    ): Call<getPostResponse>

    // 다음 게시물 5개 불러오기
    @GET("/api/post")
    fun getPost(
        @Header("Authorization") token: String/*,
        @Field("type") type: String*/,
        @Query("offset") offset : String
    ): Call<getPostResponse>
}