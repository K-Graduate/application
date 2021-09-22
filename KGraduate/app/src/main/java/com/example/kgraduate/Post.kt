package com.example.kgraduate

data class Post (
    val post_id : String,
    val post_type : String,
    val post_title : String?,
    val post_content : String?,
    val post_create_time : String?,
    val post_like : String,
    val post_comment : String,
    val post_creator_id : String?,
    val post_creator_name : String?,
    val post_image : String
)