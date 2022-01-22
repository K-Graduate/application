package com.example.kgraduate.posts

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kgraduate.R
import com.example.kgraduate.login.LoginActivity.Companion.TAG
import com.example.kgraduate.repository.entity.Post
//import kotlinx.android.synthetic.main.recylcer_post_detail_item.view.*

class DetailPostAdapter(private val mContext : Context) : RecyclerView.Adapter<DetailPostAdapter.ViewHolder>() {
    var datas = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.recylcer_post_detail_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int = datas.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //private val postImg = itemView.iv_post
        private val postImg = itemView.findViewById<ImageView>(R.id.iv_post)
        fun bind(id: String) {
            Log.d(TAG, "bind: $id")
            if(id.length > 2) {
                val uri = "http://175.123.112.88:8080/api/images/" + id
                Glide.with(itemView).load(Uri.parse(uri)).into(postImg)
            }
        }
    }
}