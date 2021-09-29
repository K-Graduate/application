package com.example.kgraduate.login

import com.example.kgraduate.login.Login
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {
    @FormUrlEncoded
    @POST("/api/login")
    fun requestLogin(
        @Field("user_id") loginId:String,
        @Field("user_pwd") loginPwd:String
    ) : Call<Login>
}