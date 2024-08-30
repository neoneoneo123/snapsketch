package com.project.snapsketch.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.snapsketch.databinding.ItemImageBinding
import com.project.snapsketch.domain.entity.ImageEntity

class HomeAdapter(
    private val homeItemListener: HomeItemListener
) : ListAdapter<ImageEntity, HomeAdapter.HomeViewHolder>(DIFF_UTIL) {

    interface HomeItemListener {
        fun onItemClicked(item: ImageEntity)
        fun onItemMenuClicked(item: ImageEntity)
    }

    inner class HomeViewHolder(
        private val binding: ItemImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImageEntity) {
            binding.apply {
                ivItem.setImageURI(item.uri)
                ivItem.setOnClickListener {
                    homeItemListener.onItemClicked(item)
                }
                ivMenu.setOnClickListener {
                    homeItemListener.onItemMenuClicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeViewHolder {
        return HomeViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<ImageEntity>() {
            override fun areItemsTheSame(oldItem: ImageEntity, newItem: ImageEntity): Boolean {
                return oldItem.uri == newItem.uri
            }

            override fun areContentsTheSame(oldItem: ImageEntity, newItem: ImageEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}