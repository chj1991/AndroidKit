package com.sys.androidkit.feature.androidktx

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sys.androidkit.feature.androidktx.databinding.ItemAndroidKtxBinding

class AndroidKtxListAdapter(
    private val onClick: (AndroidKtxEntry) -> Unit,
) : ListAdapter<AndroidKtxEntry, AndroidKtxListAdapter.Holder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemAndroidKtxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, onClick)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Holder(
        private val binding: ItemAndroidKtxBinding,
        private val onClick: (AndroidKtxEntry) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AndroidKtxEntry) {
            binding.tvTitle.text = item.title
            binding.tvSummary.text = item.summary
            binding.tvSection.text = binding.root.context.getString(
                R.string.feature_android_ktx_section,
                item.section,
            )
            binding.tvGroup.text = when (item.group) {
                AndroidKtxGroup.VIEW ->
                    binding.root.context.getString(R.string.feature_android_ktx_group_view)
                AndroidKtxGroup.CONTEXT ->
                    binding.root.context.getString(R.string.feature_android_ktx_group_context)
                AndroidKtxGroup.ACTIVITY ->
                    binding.root.context.getString(R.string.feature_android_ktx_group_activity)
                AndroidKtxGroup.UTIL ->
                    binding.root.context.getString(R.string.feature_android_ktx_group_util)
            }
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<AndroidKtxEntry>() {
        override fun areItemsTheSame(oldItem: AndroidKtxEntry, newItem: AndroidKtxEntry): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: AndroidKtxEntry,
            newItem: AndroidKtxEntry,
        ): Boolean = oldItem == newItem
    }
}
