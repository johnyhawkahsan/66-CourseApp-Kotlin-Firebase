package com.example.a66_courseapp_kotlin_firebase

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class VideoAdapter(private val videoList: List<Video>) :
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videoList[position]
        holder.titleTextView.text = video.title
        // Load the thumbnail image using a library like Picasso or Glide
        // Example using Glide:
        Glide.with(holder.itemView.context)
            .load(video.thumbnailUrl)
            .override(300, 200)
            .into(holder.thumbnailImageView)
        holder.itemView.setOnClickListener {
            // Handle the click event to open the YouTube video using an Intent
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.videoUrl))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = videoList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.videoTitle)
        val thumbnailImageView: ImageView = itemView.findViewById(R.id.videoThumbnail)
    }
}
