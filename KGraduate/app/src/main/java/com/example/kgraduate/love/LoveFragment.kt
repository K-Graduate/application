package com.example.kgraduate.love

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kgraduate.R
import com.example.kgraduate.databinding.FragmentLoveBinding
import com.example.kgraduate.login.LoginActivity.Companion.TAG
import com.example.kgraduate.login.LoginService
import com.example.kgraduate.posts.Post
import com.example.kgraduate.posts.PostAdapter
import com.example.kgraduate.posts.PostService
import kotlinx.android.synthetic.main.fragment_love.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoveFragment : Fragment() {
    lateinit var retrofit : Retrofit
    lateinit var postService: PostService

    lateinit var binding : FragmentLoveBinding
    lateinit var postAdapter: PostAdapter
    val datas = mutableListOf<Post>()

    lateinit var prefs : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoveBinding.inflate(inflater,container,false)
        prefs = context?.getSharedPreferences("Prefs", Context.MODE_PRIVATE)!!

        retrofit = Retrofit.Builder()
            .baseUrl("http://18.223.182.55:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        postService = retrofit.create(
            PostService::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postAdapter = PostAdapter(requireContext())
        rv_love.adapter = postAdapter

        datas.apply {
            add(Post(post_image = "https://i.ibb.co/3YD5hkV/YJH-2150-222.jpg", post_content = "건들면 뭅니다", post_like = "300", post_comment = "12"))
            add(Post(post_image = "https://i.ibb.co/3YD5hkV/YJH-2150-222.jpg", post_content = "건들면 뭅니다", post_like = "300", post_comment = "12"))
            add(Post(post_image = "https://i.ibb.co/3YD5hkV/YJH-2150-222.jpg", post_content = "건들면 뭅니다", post_like = "300", post_comment = "12"))
            postAdapter.postdatas = datas
            postAdapter.notifyDataSetChanged()
        }


        val authorization = prefs.getString("token","")
        postService.getPost(authorization!!).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                Log.d(TAG, "onResponse: Success!")
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.d(TAG, "onResponse: Failed!")
            }
        })
    }
}