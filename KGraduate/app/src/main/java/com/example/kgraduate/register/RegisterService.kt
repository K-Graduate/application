package com.example.kgraduate.register

import com.example.kgraduate.repository.entity.Register
import com.example.kgraduate.repository.entity.Repetition
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegisterService {
    @FormUrlEncoded
    @POST("/api/checkDuplicate")
    fun checkRepetition(
        @Field("user_id") user_id: String
    ): Call<Repetition>

    @FormUrlEncoded
    @POST("/api/register")
    fun requestRegister(
        @Field("user_id") registerId: String,
        @Field("user_pwd") registerPwd: String,
        @Field("user_name") registerName: String
    ): Call<Register>
}