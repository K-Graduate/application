package com.example.kgraduate.posts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kgraduate.R
import com.example.kgraduate.repository.entity.Post
import kotlinx.android.synthetic.main.recycler_post_item.view.*

class PostAdapter(private val context : Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    var postdatas = mutableListOf<Post>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_post_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postdatas[position])
    }

    override fun getItemCount(): Int = postdatas.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val postImg : ImageView = itemView.iv_recylcer
        private val postContent : TextView = itemView.tv_recycler
        private val postLike : TextView = itemView.tv_like_count
        private val postComment : TextView = itemView.tv_comment_count

        fun bind(item: Post) {
            Glide.with(itemView).load(item.post_image).into(postImg)
            postContent.text = item.post_content
            postLike.text = item.post_like
            postComment.text = item.post_comment
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