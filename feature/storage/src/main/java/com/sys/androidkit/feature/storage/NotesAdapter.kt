package com.sys.androidkit.feature.storage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sys.androidkit.core.database.NoteEntity
import com.sys.androidkit.feature.storage.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotesAdapter(
    private val onDelete: (NoteEntity) -> Unit,
) : ListAdapter<NoteEntity, NotesAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<NoteEntity>() {
        override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(
        private val binding: ItemNoteBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        fun bind(note: NoteEntity) {
            val tags = if (note.tags.isEmpty()) {
                binding.root.context.getString(R.string.feature_storage_room_tags_empty)
            } else {
                note.tags.joinToString(" · ")
            }
            binding.tvNote.text = buildString {
                append(note.title)
                append('\n')
                append(note.content)
                append('\n')
                append(tags)
                append(" · ")
                append(timeFormat.format(Date(note.updatedAt)))
            }
            binding.btnDelete.setOnClickListener { onDelete(note) }
        }
    }
}
