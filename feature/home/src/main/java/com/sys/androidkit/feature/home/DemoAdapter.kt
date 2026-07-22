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
    private val onFavoriteClick: ((DemoItem) -> Unit)? = null,
) : ListAdapter<DemoListRow, DemoAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<DemoListRow>() {
        override fun areItemsTheSame(oldItem: DemoListRow, newItem: DemoListRow) =
            oldItem.demo.id == newItem.demo.id

        override fun areContentsTheSame(oldItem: DemoListRow, newItem: DemoListRow) =
            oldItem == newItem
    }

    fun submitDemos(demos: List<DemoItem>, favorites: Set<String> = emptySet()) {
        submitList(demos.map { DemoListRow(it, it.id in favorites) })
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
        fun bind(row: DemoListRow) {
            val item = row.demo
            binding.tvTitle.text = item.title
            binding.tvSummary.text = item.summary
            binding.tvTags.text = item.tags.joinToString(" · ")
            binding.btnFavorite.text = if (row.favorite) "★" else "☆"
            binding.btnFavorite.setOnClickListener { onFavoriteClick?.invoke(item) }
            binding.root.setOnClickListener { onClick(item) }
        }
    }
}
