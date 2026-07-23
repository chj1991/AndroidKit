package com.sys.androidkit.feature.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sys.androidkit.feature.components.databinding.ItemComponentBinding

class ComponentListAdapter(
    private val onClick: (ComponentEntry) -> Unit,
) : ListAdapter<ComponentEntry, ComponentListAdapter.Holder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemComponentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, onClick)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Holder(
        private val binding: ItemComponentBinding,
        private val onClick: (ComponentEntry) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ComponentEntry) {
            binding.tvTitle.text = item.title
            binding.tvClass.text = item.className
            binding.tvSummary.text = item.summary
            binding.tvGroup.text = when (item.group) {
                ComponentGroup.SYSTEM ->
                    binding.root.context.getString(R.string.feature_components_group_system)
                ComponentGroup.MATERIAL3 ->
                    binding.root.context.getString(R.string.feature_components_group_m3)
            }
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<ComponentEntry>() {
        override fun areItemsTheSame(oldItem: ComponentEntry, newItem: ComponentEntry): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ComponentEntry, newItem: ComponentEntry): Boolean =
            oldItem == newItem
    }
}
