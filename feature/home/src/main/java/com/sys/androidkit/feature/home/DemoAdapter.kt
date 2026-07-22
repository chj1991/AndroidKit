package com.sys.androidkit.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sys.androidkit.core.common.model.DemoItem
import com.sys.androidkit.feature.home.databinding.ItemDemoBinding

class DemoAdapter(
    private val onClick: (DemoItem) -> Unit,
) : ListAdapter<DemoItem, DemoAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<DemoItem>() {
        override fun areItemsTheSame(oldItem: DemoItem, newItem: DemoItem) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DemoItem, newItem: DemoItem) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemDemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(
        private val binding: ItemDemoBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DemoItem) {
            binding.tvTitle.text = item.title
            binding.tvSummary.text = item.summary
            binding.tvTags.text = item.tags.joinToString(" · ")
            binding.root.setOnClickListener { onClick(item) }
        }
    }
}
