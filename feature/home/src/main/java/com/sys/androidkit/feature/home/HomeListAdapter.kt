package com.sys.androidkit.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sys.androidkit.feature.home.databinding.ItemCategoryBinding
import com.sys.androidkit.feature.home.databinding.ItemDemoBinding

class HomeListAdapter(
    private val onCategoryClick: (HomeListItem.Category) -> Unit,
    private val onDemoClick: (HomeListItem.Demo) -> Unit,
) : ListAdapter<HomeListItem, RecyclerView.ViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<HomeListItem>() {
        override fun areItemsTheSame(oldItem: HomeListItem, newItem: HomeListItem): Boolean {
            return when {
                oldItem is HomeListItem.Category && newItem is HomeListItem.Category ->
                    oldItem.data.id == newItem.data.id
                oldItem is HomeListItem.Demo && newItem is HomeListItem.Demo ->
                    oldItem.data.id == newItem.data.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: HomeListItem, newItem: HomeListItem) =
            oldItem == newItem
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is HomeListItem.Category -> TYPE_CATEGORY
        is HomeListItem.Demo -> TYPE_DEMO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_CATEGORY) {
            CategoryVH(ItemCategoryBinding.inflate(inflater, parent, false))
        } else {
            DemoVH(ItemDemoBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is HomeListItem.Category -> (holder as CategoryVH).bind(item)
            is HomeListItem.Demo -> (holder as DemoVH).bind(item)
        }
    }

    inner class CategoryVH(
        private val binding: ItemCategoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeListItem.Category) {
            binding.tvTitle.text = item.data.title
            binding.tvDescription.text = item.data.description
            binding.root.setOnClickListener { onCategoryClick(item) }
        }
    }

    inner class DemoVH(
        private val binding: ItemDemoBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeListItem.Demo) {
            binding.tvTitle.text = item.data.title
            binding.tvSummary.text = item.data.summary
            binding.tvTags.text = item.data.tags.joinToString(" · ")
            binding.root.setOnClickListener { onDemoClick(item) }
        }
    }

    companion object {
        private const val TYPE_CATEGORY = 0
        private const val TYPE_DEMO = 1
    }
}
