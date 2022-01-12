package com.example.kgraduate.repository.dto.response

import com.google.gson.JsonArray
import org.json.JSONArray

data class getPostResponse (
    val code : String,
    val posts : JsonArray
)

