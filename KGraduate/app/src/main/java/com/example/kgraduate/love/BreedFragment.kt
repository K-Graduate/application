package com.example.kgraduate.love

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kgraduate.PostDetailActivity
import com.example.kgraduate.databinding.FragmentBreedBinding
import com.example.kgraduate.login.LoginActivity.Companion.ServerUrl
import com.example.kgraduate.repository.entity.Post
import com.example.kgraduate.login.LoginActivity.Companion.TAG
import com.example.kgraduate.posts.PostAdapter
import com.example.kgraduate.posts.PostService
import com.example.kgraduate.repository.dto.response.getPostResponse
import com.google.gson.Gson
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BreedFragment : Fragment() {
    lateinit var retrofit : Retrofit
    lateinit var postService: PostService

    lateinit var binding : FragmentBreedBinding
    lateinit var postAdapter: PostAdapter
    val datas = mutableListOf<Post>()
    var lastOffset = ""

    lateinit var prefs : SharedPreferences
    lateinit var authorization : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreedBinding.inflate(inflater,container,false)
        prefs = context?.getSharedPreferences("Prefs", Context.MODE_PRIVATE)!!

        retrofit = Retrofit.Builder()
            .baseUrl(ServerUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        postService = retrofit.create(PostService::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postAdapter = PostAdapter(requireContext())
        binding.rvLove.adapter = postAdapter

        authorization = "Bearer " + prefs.getString("token","")

        postService.getFirstPost(authorization, "breed").enqueue(object : Callback<getPostResponse> {
            override fun onResponse(call: Call<getPostResponse>, response: Response<getPostResponse>) {
                Log.d(TAG, "onResponse: Success!")

                val posts = response.body()!!.posts
                addPost(posts)
            }

            override fun onFailure(call: Call<getPostResponse>, t: Throwable) {
                Log.d(TAG, "onResponse: Failed! ${t.message}")
            }
        })

        postAdapter.setPostClickListener(object : PostAdapter.PostClickListener {
            override fun postClicked(post: Post) {
                val gson = Gson()
                val intent = Intent(requireContext(), PostDetailActivity::class.java)
                intent.putExtra("post", gson.toJson(post))
                startActivity(intent)
            }
        })

        binding.rvLove.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemCount = recyclerView.adapter!!.itemCount - 1

                // ?????????????????? ????????? ??????
                if(lastPosition == itemCount && lastOffset.toInt() > 1) {
                    postService.getPost(authorization,"breed", lastOffset).enqueue(object : Callback<getPostResponse> {
                        override fun onResponse(
                            call: Call<getPostResponse>,
                            response: Response<getPostResponse>
                        ) {
                            Log.d(TAG, "onResponse: Success!")

                            val posts = response.body()!!.posts
                            addPost(posts)
                        }

                        override fun onFailure(call: Call<getPostResponse>, t: Throwable) {
                            Log.d(TAG, "onResponse: Failed! ${t.message}")
                        }
                    })
                }
            }
        })
    }

    fun addPost(posts: JsonArray) {
        for(i in 0 until posts.size()) {
            // json to Post ??????
            val gson = Gson()
            val post = gson.fromJson(posts.get(i), Post::class.java)

            // post???????????? ??????
            datas.add(post)

            lastOffset = post.id
        }
        // ????????? ????????? ????????????
        Log.d(TAG, "love fragment > post data ??????: $datas")
        postAdapter.postdatas = datas
        postAdapter.notifyDataSetChanged()
    }
}