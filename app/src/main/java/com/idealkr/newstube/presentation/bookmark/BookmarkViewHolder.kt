package com.idealkr.newstube.presentation.bookmark

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idealkr.newstube.databinding.ItemVideoHomeBinding
import com.idealkr.newstube.domain.model.VideoInfo

class BookmarkViewHolder(
    private val binding: ItemVideoHomeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: VideoInfo) {
        itemView.apply {
            Glide
                .with(context)
                .load(data.thumbnail)
                .centerCrop()
                .into(binding.imageView)

            binding.textViewDate.text = data.dateTime
            binding.textViewTitle.text = data.title
        }
    }
}