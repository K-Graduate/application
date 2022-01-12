package com.example.kgraduate.login

import com.example.kgraduate.repository.entity.Login
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {
    @FormUrlEncoded
    @POST("/api/auth")
    fun requestLogin(
        @Field("user_id") loginId:String,
        @Field("user_pwd") loginPwd:String
    ) : Call<Login>
}