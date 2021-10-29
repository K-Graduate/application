package com.example.kgraduate.write

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kgraduate.R
import kotlinx.android.synthetic.main.image_item.view.*

class MultiImageAdapter(private val context: Context, private val listener : onRemoveClick) : RecyclerView.Adapter<MultiImageAdapter.ViewHolder>() {
    var mData = mutableListOf<Uri>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiImageAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.image_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MultiImageAdapter.ViewHolder, position: Int) {
        val image_uri = mData[position]
        Glide.with(context).load(image_uri).into(holder.image)

        holder.button.setOnClickListener {
            mData.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()

            listener.onRemoveClicked(mData.size)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image : ImageView = itemView.image
        val button : Button = itemView.btn_recycler_item
    }
}