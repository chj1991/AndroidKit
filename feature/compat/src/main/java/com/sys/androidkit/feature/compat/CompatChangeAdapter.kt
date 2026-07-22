package com.sys.androidkit.feature.compat

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sys.androidkit.feature.compat.databinding.ItemCompatChangeBinding

class CompatChangeAdapter :
    ListAdapter<CompatChange, CompatChangeAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<CompatChange>() {
        override fun areItemsTheSame(oldItem: CompatChange, newItem: CompatChange) =
            oldItem.api == newItem.api && oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: CompatChange, newItem: CompatChange) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCompatChangeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(
        private val binding: ItemCompatChangeBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CompatChange) {
            val context = binding.root.context
            binding.tvApi.text = context.getString(
                R.string.feature_compat_api_line,
                item.api,
                item.codename,
            )
            binding.tvTitle.text = item.title
            binding.tvSummary.text = item.summary
            val applies = Build.VERSION.SDK_INT >= item.api
            binding.tvBadge.visibility = if (applies) View.VISIBLE else View.GONE
            binding.tvBadge.text = context.getString(R.string.feature_compat_applies)
        }
    }
}
