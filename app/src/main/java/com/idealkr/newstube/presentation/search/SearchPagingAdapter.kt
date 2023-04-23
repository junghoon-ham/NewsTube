package com.idealkr.newstube.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idealkr.newstube.databinding.ItemVideoHomeBinding
import com.idealkr.newstube.domain.model.Documents
import com.idealkr.newstube.util.toFormatDateString

class SearchPagingAdapter :
    PagingDataAdapter<Documents, SearchPagingAdapter.SearchViewHolder>(DocumentsDiffCallback) {

    private var onItemClickListener: ((Documents) -> Unit)? = null
    fun setOnItemClickListener(listener: (Documents) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)

        item?.let { data ->
            holder.bind(data, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemVideoHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    companion object {
        private val DocumentsDiffCallback = object : DiffUtil.ItemCallback<Documents>() {
            override fun areItemsTheSame(oldItem: Documents, newItem: Documents): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Documents, newItem: Documents): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class SearchViewHolder(
        private val binding: ItemVideoHomeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            documents: Documents,
            position: Int
        ) {
            binding.apply {
                itemView.apply {
                    Glide
                        .with(context)
                        .load(documents.thumbnailUrl)
                        .centerCrop()
                        .into(imageView)

                    textViewTitle.text = documents.displaySitename
                    textViewDate.text = documents.datetime?.toFormatDateString()

                    root.setOnClickListener {
                        onItemClickListener?.let { it(documents) }
                    }
                }
            }
        }
    }
}