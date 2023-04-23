package com.idealkr.newstube.presentation.home.video_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.idealkr.newstube.databinding.ItemVideoHomeBinding
import com.idealkr.newstube.domain.model.Documents

class VideoPagingAdapter : PagingDataAdapter<Documents, VideoViewHolder>(VideoDiffCallback) {

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val pagedBook = getItem(position)
        pagedBook?.let { documents ->
            holder.bind(documents)
            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(documents) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            ItemVideoHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private var onItemClickListener: ((Documents) -> Unit)? = null
    fun setOnItemClickListener(listener: (Documents) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val VideoDiffCallback = object : DiffUtil.ItemCallback<Documents>() {
            override fun areItemsTheSame(oldItem: Documents, newItem: Documents): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Documents, newItem: Documents): Boolean {
                return oldItem == newItem
            }
        }
    }
}