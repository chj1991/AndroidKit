package com.sys.androidkit.feature.storage

import androidx.lifecycle.viewModelScope
import com.sys.androidkit.core.database.NoteDao
import com.sys.androidkit.core.database.NoteEntity
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class RoomLabViewModel @Inject constructor(
    private val noteDao: NoteDao,
) : BaseViewModel() {

    val notes: StateFlow<List<NoteEntity>> = noteDao.observeNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addNote() {
        launch {
            val index = notes.value.size + 1
            noteDao.insert(
                NoteEntity(
                    title = "笔记 $index",
                    content = "Room Flow 自动刷新",
                ),
            )
        }
    }

    fun delete(note: NoteEntity) {
        launch { noteDao.delete(note) }
    }
}
