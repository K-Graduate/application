package com.example.kgraduate.repository.entity


data class Post(
    val id: String,
    val type: String,
    val title: String,
    val content: String,
    val create_time: String,
    val like: String,
    val creator_name: String,
    val file_id: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val post_creator_id: String
)

// 아이디 등록 클래스
data class Register(
    val code: String,
    val message: String
)

// 아이디 중복 확인 클래스
data class Repetition(
    val code: Int,
    val message: String
)

data class Login(
    val code: String,
    val token: String
)

