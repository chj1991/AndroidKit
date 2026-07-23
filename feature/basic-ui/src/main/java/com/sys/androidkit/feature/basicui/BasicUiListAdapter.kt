package com.sys.androidkit.feature.basicui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sys.androidkit.feature.basicui.databinding.ItemBasicUiBinding

class BasicUiListAdapter(
    private val onClick: (BasicUiEntry) -> Unit,
) : ListAdapter<BasicUiEntry, BasicUiListAdapter.Holder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemBasicUiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, onClick)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Holder(
        private val binding: ItemBasicUiBinding,
        private val onClick: (BasicUiEntry) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BasicUiEntry) {
            binding.tvTitle.text = item.title
            binding.tvSummary.text = item.summary
            binding.tvWiki.text = binding.root.context.getString(
                R.string.feature_basic_ui_wiki,
                item.wiki,
            )
            binding.tvGroup.text = when (item.group) {
                BasicUiGroup.WIDGET ->
                    binding.root.context.getString(R.string.feature_basic_ui_group_widget)
                BasicUiGroup.RECYCLER ->
                    binding.root.context.getString(R.string.feature_basic_ui_group_rv)
                BasicUiGroup.UTILS ->
                    binding.root.context.getString(R.string.feature_basic_ui_group_utils)
            }
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<BasicUiEntry>() {
        override fun areItemsTheSame(oldItem: BasicUiEntry, newItem: BasicUiEntry): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: BasicUiEntry, newItem: BasicUiEntry): Boolean =
            oldItem == newItem
    }
}
