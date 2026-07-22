package com.sys.androidkit.feature.storage

import android.view.LayoutInflater
import android.view.ViewGroup
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
    private val adapter = NotesAdapter { confirmDelete(it) }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentRoomBinding = FragmentRoomBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.tvTips.text = getString(R.string.feature_storage_room_tips, viewModel.dbVersion)
        binding.btnAdd.setOnClickListener { viewModel.addNote() }
        binding.rvNotes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotes.adapter = adapter
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.notes.collect { adapter.submitList(it) }
                }
                launch {
                    viewModel.events.collect { event ->
                        when (event) {
                            is RoomLabEvent.Added -> {
                                showMessage(
                                    getString(R.string.feature_storage_room_added, event.title),
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
                        }
                    }
                }
            }
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
