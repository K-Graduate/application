package com.example.kgraduate.posts

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kgraduate.R
import com.example.kgraduate.login.LoginActivity.Companion.TAG
import com.example.kgraduate.repository.entity.Post
//import kotlinx.android.synthetic.main.recycler_post_item.view.*

class PostAdapter(private val context : Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    var postdatas = mutableListOf<Post>()
    private lateinit var mPostClickListener: PostClickListener

    interface PostClickListener {
        fun postClicked(post : Post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_post_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postdatas[position])

        holder.itemView.setOnClickListener {
            mPostClickListener.postClicked(postdatas[position])
        }
    }

    override fun getItemCount(): Int = postdatas.size

    fun setPostClickListener(postClickListener: PostClickListener) {
        mPostClickListener = postClickListener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val postImg : ImageView = itemView.findViewById(R.id.iv_recylcer)
        private val postContent : TextView = itemView.findViewById(R.id.tv_recycler)
        private val postLike : TextView = itemView.findViewById(R.id.tv_like_count)
        private val postComment : TextView = itemView.findViewById(R.id.tv_comment_count)

        fun bind(item: Post) {
            // 사진 등록
            //Glide.with(itemView).load(item.post_image).into(postImg)
            val arr = item.file_id.split("|")
//            Log.d(TAG, "postAdapter > split before : ${item}")
//            Log.d(TAG, "postAdapter > split result : $arr")
//            Log.d(TAG, "postAdapter > file id : ${arr[0]}")
            val uri = "http://175.123.112.88:8080/api/images/" + arr[0] + ".jpg"
            Glide.with(itemView).load(Uri.parse(uri)).into(postImg)
            postContent.text = item.content
            postLike.text = item.like
            // 댓글
            //postComment.text = item.post_comment
        }
    }


//        private val txtName: TextView = itemView.findViewById(R.id.tv_rv_name)
//        private val txtAge: TextView = itemView.findViewById(R.id.tv_rv_age)
//        private val imgProfile: ImageView = itemView.findViewById(R.id.img_rv_photo)
//
//        fun bind(item: ProfileData) {
//            txtName.text = item.name
//            txtAge.text = item.age.toString()
//            Glide.with(itemView).load(item.img).into(imgProfile)
//
//        }
}