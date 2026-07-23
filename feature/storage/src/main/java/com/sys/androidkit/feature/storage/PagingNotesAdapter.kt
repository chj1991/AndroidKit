package com.sys.androidkit.feature.storage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sys.androidkit.core.common.time.DateFormats
import com.sys.androidkit.core.database.NoteWithAuthor
import com.sys.androidkit.feature.storage.databinding.ItemNoteBinding

class PagingNotesAdapter : PagingDataAdapter<NoteWithAuthor, PagingNotesAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<NoteWithAuthor>() {
        override fun areItemsTheSame(oldItem: NoteWithAuthor, newItem: NoteWithAuthor) =
            oldItem.note.id == newItem.note.id

        override fun areContentsTheSame(oldItem: NoteWithAuthor, newItem: NoteWithAuthor) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class VH(
        private val binding: ItemNoteBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(row: NoteWithAuthor) {
            val note = row.note
            val author = row.author?.name ?: "（无作者）"
            binding.tvNote.text = buildString {
                append('#')
                append(note.id)
                append(' ')
                append(note.title)
                append('\n')
                append(note.content)
                append('\n')
                append("作者：")
                append(author)
                append(" · ")
                append(DateFormats.relativeToNow(note.createdAt))
            }
            binding.btnEdit.visibility = android.view.View.GONE
            binding.btnDelete.visibility = android.view.View.GONE
        }
    }
}
