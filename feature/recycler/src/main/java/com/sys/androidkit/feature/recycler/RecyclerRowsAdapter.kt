package com.sys.androidkit.feature.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sys.androidkit.feature.recycler.databinding.ItemRecyclerHeaderBinding
import com.sys.androidkit.feature.recycler.databinding.ItemRecyclerRowBinding

class RecyclerRowsAdapter(
    private val onItemClick: (RecyclerRow.Item) -> Unit,
) : ListAdapter<RecyclerRow, RecyclerView.ViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<RecyclerRow>() {
        override fun areItemsTheSame(oldItem: RecyclerRow, newItem: RecyclerRow): Boolean {
            return when {
                oldItem is RecyclerRow.Header && newItem is RecyclerRow.Header ->
                    oldItem.id == newItem.id
                oldItem is RecyclerRow.Item && newItem is RecyclerRow.Item ->
                    oldItem.id == newItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: RecyclerRow, newItem: RecyclerRow) =
            oldItem == newItem
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is RecyclerRow.Header -> TYPE_HEADER
        is RecyclerRow.Item -> TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            HeaderVH(ItemRecyclerHeaderBinding.inflate(inflater, parent, false))
        } else {
            ItemVH(ItemRecyclerRowBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val row = getItem(position)) {
            is RecyclerRow.Header -> (holder as HeaderVH).bind(row)
            is RecyclerRow.Item -> (holder as ItemVH).bind(row)
        }
    }

    inner class HeaderVH(
        private val binding: ItemRecyclerHeaderBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(row: RecyclerRow.Header) {
            binding.tvHeader.text = row.title
        }
    }

    inner class ItemVH(
        private val binding: ItemRecyclerRowBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(row: RecyclerRow.Item) {
            binding.tvRow.text = row.title
            binding.root.setOnClickListener { onItemClick(row) }
        }
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }
}
