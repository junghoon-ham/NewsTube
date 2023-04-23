package com.idealkr.newstube.presentation.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.idealkr.newstube.databinding.ItemVideoHomeBinding
import com.idealkr.newstube.domain.model.VideoInfo

//TODO: VideoPagingAdapter 합칠 예정
class BookmarkPagingAdapter : PagingDataAdapter<VideoInfo, BookmarkViewHolder>(VideoDiffCallback) {

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val pagedBook = getItem(position)
        pagedBook?.let { video ->
            holder.bind(video)
            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(video) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        return BookmarkViewHolder(
            ItemVideoHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private var onItemClickListener: ((VideoInfo) -> Unit)? = null
    fun setOnItemClickListener(listener: (VideoInfo) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val VideoDiffCallback = object : DiffUtil.ItemCallback<VideoInfo>() {
            override fun areItemsTheSame(oldItem: VideoInfo, newItem: VideoInfo): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: VideoInfo, newItem: VideoInfo): Boolean {
                return oldItem == newItem
            }
        }
    }
}