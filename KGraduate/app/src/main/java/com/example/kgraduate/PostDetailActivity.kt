package com.example.kgraduate

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.kgraduate.databinding.ActivityPostDetailBinding
import com.example.kgraduate.login.LoginActivity.Companion.TAG
import com.example.kgraduate.posts.DetailPostAdapter
import com.example.kgraduate.repository.entity.Post
import com.google.gson.Gson

class PostDetailActivity : AppCompatActivity() {
    lateinit var binding : ActivityPostDetailBinding
    lateinit var detailPostAdapter: DetailPostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        init()
    }

    fun setPost(post : Post) {
        Log.d(TAG, "setPost: $post")
        binding.tvId.text = "왜안돼"
        binding.tvTime.text = post.create_time

        var ids = post.file_id.split("|")
        detailPostAdapter.datas = ids as MutableList
    }

    fun init() {
        binding = ActivityPostDetailBinding.inflate(layoutInflater)

        detailPostAdapter = DetailPostAdapter(baseContext)
        binding.rvPost.adapter = detailPostAdapter

        // 뒤로가기 버튼
        binding.tvBack.setOnClickListener {
            finish()
        }

        if(intent.hasExtra("post")) {
            Log.d(TAG, "intent 수신 성공")
            val gson = Gson()
            val post = gson.fromJson(intent.getStringExtra("post"), Post::class.java)
            setPost(post)
        }
    }
}