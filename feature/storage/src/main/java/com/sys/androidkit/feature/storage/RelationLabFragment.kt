package com.sys.androidkit.feature.storage

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sys.androidkit.core.common.time.DateFormats
import com.sys.androidkit.core.database.AuthorEntity
import com.sys.androidkit.core.database.AuthorWithNotes
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.core.ui.ext.showConfirmDialog
import com.sys.androidkit.feature.storage.databinding.FragmentRelationBinding
import com.sys.androidkit.feature.storage.databinding.ItemAuthorNotesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RelationLabFragment : BaseFragment<FragmentRelationBinding>() {

    private val viewModel: RelationLabViewModel by viewModels()
    private val adapter = AuthorNotesAdapter(
        onAddNote = { viewModel.addNoteFor(it) },
        onRename = { promptRename(it) },
        onDeleteAuthor = { confirmDeleteAuthor(it) },
        onDeleteLastNote = { viewModel.deleteLastNoteOf(it.id) },
    )

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentRelationBinding = FragmentRelationBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.stateView.contentContainer.removeAllViews()
        val list = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@RelationLabFragment.adapter
            clipToPadding = false
        }
        binding.stateView.contentContainer.addView(
            list,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            ),
        )
        binding.stateView.setOnRetry { viewModel.ensureSeedAuthors() }
        binding.btnSeed.setOnClickListener { viewModel.ensureSeedAuthors() }
        binding.btnAdd.setOnClickListener { viewModel.addNoteRoundRobin() }
        binding.btnAddAuthor.setOnClickListener { promptAddAuthor() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.stateView.render(state.demoState)
                    adapter.submitList(state.rows)
                }
            }
        }
    }

    private fun promptAddAuthor() {
        val input = EditText(requireContext()).apply {
            hint = getString(R.string.feature_storage_relation_author_hint)
            setPadding(48, 32, 48, 32)
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.feature_storage_relation_add_author)
            .setView(input)
            .setPositiveButton(R.string.feature_storage_save) { _, _ ->
                viewModel.addAuthor(input.text?.toString().orEmpty())
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun promptRename(author: AuthorEntity) {
        val input = EditText(requireContext()).apply {
            setText(author.name)
            setPadding(48, 32, 48, 32)
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.feature_storage_relation_rename)
            .setView(input)
            .setPositiveButton(R.string.feature_storage_save) { _, _ ->
                viewModel.renameAuthor(author, input.text?.toString().orEmpty())
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun confirmDeleteAuthor(author: AuthorEntity) {
        showConfirmDialog(
            title = getString(R.string.feature_storage_relation_delete_author),
            message = getString(R.string.feature_storage_relation_delete_author_msg, author.name),
            positive = getString(R.string.feature_storage_delete),
            onPositive = { viewModel.deleteAuthor(author) },
        )
    }
}

private class AuthorNotesAdapter(
    private val onAddNote: (AuthorEntity) -> Unit,
    private val onRename: (AuthorEntity) -> Unit,
    private val onDeleteAuthor: (AuthorEntity) -> Unit,
    private val onDeleteLastNote: (AuthorEntity) -> Unit,
) : ListAdapter<AuthorWithNotes, AuthorNotesAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<AuthorWithNotes>() {
        override fun areItemsTheSame(oldItem: AuthorWithNotes, newItem: AuthorWithNotes) =
            oldItem.author.id == newItem.author.id

        override fun areContentsTheSame(oldItem: AuthorWithNotes, newItem: AuthorWithNotes) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemAuthorNotesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(
        private val binding: ItemAuthorNotesBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(row: AuthorWithNotes) {
            binding.tvAuthor.text = "${row.author.name} · id=${row.author.id} · ${row.notes.size} 篇"
            binding.tvNotes.text = if (row.notes.isEmpty()) {
                "（暂无笔记）长按「加笔记」旁删除可删最后一篇"
            } else {
                row.notes.joinToString("\n") { note ->
                    "· #${note.id} ${note.title} · ${DateFormats.relativeToNow(note.updatedAt)}"
                }
            }
            binding.btnAddNote.setOnClickListener { onAddNote(row.author) }
            binding.btnAddNote.setOnLongClickListener {
                onDeleteLastNote(row.author)
                true
            }
            binding.btnRename.setOnClickListener { onRename(row.author) }
            binding.btnDeleteAuthor.setOnClickListener { onDeleteAuthor(row.author) }
        }
    }
}
