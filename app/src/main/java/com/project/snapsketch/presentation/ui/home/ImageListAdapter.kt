package com.project.snapsketch.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.snapsketch.databinding.ItemImageBinding
import com.project.snapsketch.presentation.model.ImageModel

class ImageListAdapter(
    private val imageItemListener: ImageItemListener
) : ListAdapter<ImageModel, ImageListAdapter.ImageListViewHolder>(DIFF_UTIL) {

    interface ImageItemListener {
        fun onItemClicked(item: ImageModel)
    }

    inner class ImageListViewHolder(
        private val binding: ItemImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImageModel) {
            binding.ivItem.setImageURI(item.uri)

            binding.root.setOnClickListener {
                imageItemListener.onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageListViewHolder {
        return ImageListViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<ImageModel>() {
            override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
                return oldItem.uri == newItem.uri
            }

            override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}