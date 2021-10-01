package com.example.kgraduate.love

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kgraduate.R
import com.example.kgraduate.databinding.FragmentLoveBinding
import com.example.kgraduate.posts.Post
import com.example.kgraduate.posts.PostAdapter
import kotlinx.android.synthetic.main.fragment_love.*

class LoveFragment : Fragment() {
    lateinit var binding : FragmentLoveBinding
    lateinit var postAdapter: PostAdapter
    val datas = mutableListOf<Post>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoveBinding.inflate(inflater,container,false)
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
    }
}