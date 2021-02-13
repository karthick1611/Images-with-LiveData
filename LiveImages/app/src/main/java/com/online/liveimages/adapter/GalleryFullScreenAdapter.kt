package com.online.liveimages.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.online.liveimages.R
import com.online.liveimages.model.Photo

class GalleryFullScreenAdapter(private var photoList: List<Photo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return GalleryFullScreenAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.gallery_full_screen_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GalleryFullScreenAdapterViewHolder -> {
                holder.bind(photoList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    inner class GalleryFullScreenAdapterViewHolder
    constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        var image_iv: ImageView = itemView.findViewById(R.id.image_iv)

        fun bind(item: Photo) {

            Glide.with(itemView)
                .load(item.src.large)
                .into(image_iv)

        }
    }
}
