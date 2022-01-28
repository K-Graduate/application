package com.example.kgraduate

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
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

        init()
    }

    fun setPost(post : Post) {
        binding.tvId.text = post.creator_name
        binding.tvTime.text = post.create_time

        val file_id = post.file_id.substring(0,post.file_id.length-1)
        var ids = file_id.split("|")
        detailPostAdapter.datas = ids as MutableList
        detailPostAdapter.notifyDataSetChanged()
    }

    fun init() {
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewPager의 Paging 방향은 Horizontal
        binding.rvPost.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.rvPost.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            // Paging 완료되면 호출
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("ViewPagerFragment", "Page ${position+1}")
            }
        })

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