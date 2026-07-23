package com.sys.androidkit.feature.performance

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sys.androidkit.core.common.log.LogEntry
import com.sys.androidkit.feature.performance.databinding.ItemLogEntryBinding

class LogViewerAdapter : ListAdapter<LogEntry, LogViewerAdapter.Holder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemLogEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Holder(
        private val binding: ItemLogEntryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: LogEntry) {
            binding.tvLine.text = entry.formatLine()
            binding.tvLine.setTextColor(colorFor(entry.priority))
        }

        private fun colorFor(priority: Int): Int = when (priority) {
            Log.VERBOSE, Log.DEBUG -> Color.parseColor("#546E7A")
            Log.INFO -> Color.parseColor("#1565C0")
            Log.WARN -> Color.parseColor("#EF6C00")
            Log.ERROR, Log.ASSERT -> Color.parseColor("#C62828")
            else -> Color.DKGRAY
        }
    }

    private object Diff : DiffUtil.ItemCallback<LogEntry>() {
        override fun areItemsTheSame(oldItem: LogEntry, newItem: LogEntry): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: LogEntry, newItem: LogEntry): Boolean =
            oldItem == newItem
    }
}
