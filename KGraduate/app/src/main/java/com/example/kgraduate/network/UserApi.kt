package com.example.kgraduate.network

import com.example.kgraduate.repository.entity.Login
import com.example.kgraduate.repository.entity.Register
import com.example.kgraduate.repository.entity.Repetition
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApi {

    @FormUrlEncoded
    @POST("/api/auth")
    fun requestLogin1(
        @Field("user_id") loginId:String,
        @Field("user_pwd") loginPwd:String
    ) : Call<Login>

    @FormUrlEncoded
    @POST("/api/checkDuplicate")
    fun checkRepitition1(
        @Field("user_id") registerId: String
    ): Call<Repetition>

    @FormUrlEncoded
    @POST("/api/register")
    fun requestRegister1(
        @Field("user_id") registerId: String,
        @Field("user_pwd") registerPwd: String,
        @Field("user_name") registerName: String
    ): Call<Register>
}