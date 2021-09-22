package com.example.kgraduate.Register

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegisterService {
    @FormUrlEncoded
    @POST("/api/register")
    fun requestLogin(
        @Field("user_id") registerId:String,
        @Field("user_pwd") registerPwd:String,
        @Field("user_name") registerName:String
    ) : Call<Register>
}