package com.idealkr.newstube.presentation.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idealkr.newstube.databinding.ItemChipBinding

class FilterAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataList = arrayListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            ItemChipBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).apply {
            bindItem(dataList[position])
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(val binding: ItemChipBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: String) {
            with(binding.root) {
                text = item
                isChecked = true
                isCheckable = false
            }
        }
    }

    fun setData(newList: ArrayList<String>) {
        dataList = newList
        notifyDataSetChanged()
    }
}