package com.example.kgraduate.love

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kgraduate.databinding.FragmentLoveBinding
import com.example.kgraduate.repository.entity.Post
import com.example.kgraduate.login.LoginActivity.Companion.TAG
import com.example.kgraduate.posts.PostAdapter
import com.example.kgraduate.posts.PostService
import com.example.kgraduate.repository.dto.response.getPostResponse
import com.google.gson.Gson
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
            //.baseUrl("http://175.123.112.88:8080")
            .baseUrl("http://18.223.182.55:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        postService = retrofit.create(PostService::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postAdapter = PostAdapter(requireContext())
        binding.rvLove.adapter = postAdapter


        val authorization = "Bearer " + prefs.getString("token","")
        postService.getPost(authorization).enqueue(object : Callback<getPostResponse> {
            override fun onResponse(call: Call<getPostResponse>, response: Response<getPostResponse>) {
                Log.d(TAG, "onResponse: Success!")
                val posts = response.body()!!.posts
                for(i in 0 until posts.size()) {
                    val post = posts.get(i)
                    val gson = Gson()

                    datas.add(gson.fromJson(post,Post::class.java))
                }
                postAdapter.postdatas = datas
                postAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<getPostResponse>, t: Throwable) {
                Log.d(TAG, "onResponse: Failed! ${t.message}")
            }
        })
    }
}