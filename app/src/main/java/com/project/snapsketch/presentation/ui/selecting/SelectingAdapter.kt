package com.project.snapsketch.presentation.ui.selecting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.snapsketch.databinding.ItemSelectingBinding
import com.project.snapsketch.domain.entity.ImageEntity

class SelectingAdapter(
    private val selectingItemListener: SelectingItemListener
) : ListAdapter<ImageEntity, SelectingAdapter.SelectingViewHolder>(DIFF_UTIL) {

    interface SelectingItemListener {
        fun onItemClicked(item: ImageEntity)
    }

    inner class SelectingViewHolder(
        private val binding: ItemSelectingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImageEntity) {
            binding.apply {
                ivSelectingItem.setImageURI(item.uriString?.toUri())
                ivSelectingItem.setOnClickListener {
                    selectingItemListener.onItemClicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectingViewHolder {
        return SelectingViewHolder(
            ItemSelectingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SelectingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<ImageEntity>() {
            override fun areItemsTheSame(oldItem: ImageEntity, newItem: ImageEntity): Boolean {
                return oldItem.uriString == newItem.uriString
            }

            override fun areContentsTheSame(oldItem: ImageEntity, newItem: ImageEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}