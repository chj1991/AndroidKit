package com.sys.androidkit.feature.storage

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sys.androidkit.core.database.NoteEntity
import com.sys.androidkit.feature.storage.databinding.DialogNoteEditBinding

fun Fragment.showNoteEditDialog(
    note: NoteEntity? = null,
    onConfirm: (title: String, content: String, tagsCsv: String) -> Unit,
) {
    val binding = DialogNoteEditBinding.inflate(LayoutInflater.from(requireContext()))
    if (note != null) {
        binding.etTitle.setText(note.title)
        binding.etContent.setText(note.content)
        binding.etTags.setText(note.tags.joinToString(","))
    } else {
        binding.etTags.setText("room,mvvm")
    }
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(
            if (note == null) {
                getString(R.string.feature_storage_room_dialog_add)
            } else {
                getString(R.string.feature_storage_room_dialog_edit)
            },
        )
        .setView(binding.root)
        .setPositiveButton(R.string.feature_storage_save) { _, _ ->
            onConfirm(
                binding.etTitle.text?.toString().orEmpty(),
                binding.etContent.text?.toString().orEmpty(),
                binding.etTags.text?.toString().orEmpty(),
            )
        }
        .setNegativeButton(android.R.string.cancel, null)
        .show()
}
