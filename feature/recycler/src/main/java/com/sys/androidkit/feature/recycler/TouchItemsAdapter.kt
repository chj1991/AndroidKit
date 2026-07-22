package com.sys.androidkit.feature.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sys.androidkit.feature.recycler.databinding.ItemTouchRowBinding

class TouchItemsAdapter : RecyclerView.Adapter<TouchItemsAdapter.VH>() {

    private val items = mutableListOf<TouchItem>()

    fun submit(list: List<TouchItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun move(from: Int, to: Int) {
        if (from !in items.indices || to !in items.indices) return
        val item = items.removeAt(from)
        items.add(to, item)
        notifyItemMoved(from, to)
    }

    fun removeAt(index: Int): TouchItem? {
        if (index !in items.indices) return null
        val removed = items.removeAt(index)
        notifyItemRemoved(index)
        return removed
    }

    fun snapshot(): List<TouchItem> = items.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemTouchRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class VH(
        private val binding: ItemTouchRowBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TouchItem) {
            binding.tvTitle.text = item.title
            binding.tvSubtitle.text = "id=${item.id}"
        }
    }
}
