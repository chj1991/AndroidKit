package com.sys.androidkit.feature.storage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sys.androidkit.core.database.NoteEntity
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.core.ui.ext.showConfirmDialog
import com.sys.androidkit.feature.storage.databinding.FragmentRoomBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoomLabFragment : BaseFragment<FragmentRoomBinding>() {

    private val viewModel: RoomLabViewModel by viewModels()
    private val adapter = NotesAdapter(
        onEdit = { showEdit(it) },
        onDelete = { confirmDelete(it) },
    )

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentRoomBinding = FragmentRoomBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.tvTips.text = getString(R.string.feature_storage_room_tips, viewModel.dbVersion)
        binding.etSearch.doAfterTextChanged { viewModel.onQueryChanged(it?.toString().orEmpty()) }
        binding.btnAdd.setOnClickListener {
            showNoteEditDialog { title, content, tags ->
                viewModel.addNote(title, content, tags)
            }
        }
        binding.btnClearAll.setOnClickListener {
            showConfirmDialog(
                title = getString(R.string.feature_storage_room_clear_title),
                message = getString(R.string.feature_storage_room_clear_message),
                positive = getString(R.string.feature_storage_room_clear_all),
                onPositive = { viewModel.clearAll() },
            )
        }
        binding.rvNotes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotes.adapter = adapter
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        adapter.submitList(state.notes)
                        binding.tvCount.text = getString(
                            R.string.feature_storage_room_count,
                            state.notes.size,
                            state.totalCount,
                        )
                    }
                }
                launch {
                    viewModel.events.collect { event ->
                        when (event) {
                            is RoomLabEvent.Added -> {
                                showMessage(
                                    getString(R.string.feature_storage_room_added, event.title),
                                )
                            }
                            is RoomLabEvent.Updated -> {
                                showMessage(
                                    getString(R.string.feature_storage_room_updated, event.title),
                                )
                            }
                            is RoomLabEvent.Deleted -> {
                                showMessage(
                                    message = getString(
                                        R.string.feature_storage_room_deleted,
                                        event.note.title,
                                    ),
                                    length = Snackbar.LENGTH_LONG,
                                    actionLabel = getString(R.string.feature_storage_room_undo),
                                    action = { viewModel.restore(event.note) },
                                )
                            }
                            RoomLabEvent.Cleared -> {
                                showMessage(R.string.feature_storage_room_cleared)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showEdit(note: NoteEntity) {
        showNoteEditDialog(note) { title, content, tags ->
            viewModel.updateNote(note, title, content, tags)
        }
    }

    private fun confirmDelete(note: NoteEntity) {
        showConfirmDialog(
            title = getString(R.string.feature_storage_room_delete_title),
            message = getString(R.string.feature_storage_room_delete_message, note.title),
            positive = getString(R.string.feature_storage_delete),
            onPositive = { viewModel.delete(note) },
        )
    }
}
