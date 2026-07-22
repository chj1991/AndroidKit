package com.sys.androidkit.feature.network

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sys.androidkit.feature.network.databinding.ItemPostBinding

class PostsAdapter : ListAdapter<PostDto, PostsAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<PostDto>() {
        override fun areItemsTheSame(oldItem: PostDto, newItem: PostDto) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PostDto, newItem: PostDto) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(
        private val binding: ItemPostBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostDto) {
            binding.tvTitle.text = post.title
            binding.tvBody.text = post.body
        }
    }
}
