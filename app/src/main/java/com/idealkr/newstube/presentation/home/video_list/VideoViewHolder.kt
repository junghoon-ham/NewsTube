package com.idealkr.newstube.presentation.home.video_list

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idealkr.newstube.databinding.ItemVideoHomeBinding
import com.idealkr.newstube.domain.model.Documents
import com.idealkr.newstube.util.toFormatDateString

class VideoViewHolder(
    private val binding: ItemVideoHomeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: Documents) {
        itemView.apply {
            Glide
                .with(context)
                .load(data.thumbnailUrl)
                .centerCrop()
                .into(binding.imageView)

            binding.textViewDate.text = data.datetime?.toFormatDateString()
            binding.textViewTitle.text = data.displaySitename
        }
    }
}