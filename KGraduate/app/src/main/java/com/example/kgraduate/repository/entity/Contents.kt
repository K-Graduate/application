package com.example.kgraduate.repository.entity

data class Post(
    val post_id: String,
    val post_type: String,
    val post_title: String,
    val post_content: String,
    val post_create_time: String,
    val post_like: String,
    val post_comment: String,
    val post_creator_id: String,
    val post_creator_name: String,
    val post_image: String
)

// 아이디 등록 클래스
data class Register(
    val code: String,
    val message: String
)

// 아이디 중복 확인 클래스
data class Repetition(
    val code: String
)

data class Login(
    val code: String,
    val token: String
)

