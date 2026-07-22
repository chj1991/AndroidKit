package com.sys.androidkit.feature.storage

import androidx.lifecycle.viewModelScope
import com.sys.androidkit.core.database.AppDatabase
import com.sys.androidkit.core.database.NoteDao
import com.sys.androidkit.core.database.NoteEntity
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn

sealed interface RoomLabEvent {
    data class Added(val title: String) : RoomLabEvent
    data class Deleted(val note: NoteEntity) : RoomLabEvent
}

@HiltViewModel
class RoomLabViewModel @Inject constructor(
    private val noteDao: NoteDao,
) : BaseViewModel() {

    val dbVersion: Int = AppDatabase.VERSION

    val notes: StateFlow<List<NoteEntity>> = noteDao.observeNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _events = MutableSharedFlow<RoomLabEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<RoomLabEvent> = _events.asSharedFlow()

    fun addNote() {
        launch {
            val index = notes.value.size + 1
            val now = System.currentTimeMillis()
            val note = NoteEntity(
                title = "笔记 $index",
                content = "Room Flow 自动刷新 · Migration v$dbVersion",
                tags = listOf("room", "mvvm", if (index % 2 == 0) "demo" else "kit"),
                createdAt = now,
                updatedAt = now,
            )
            noteDao.insert(note)
            _events.emit(RoomLabEvent.Added(note.title))
        }
    }

    fun delete(note: NoteEntity) {
        launch {
            noteDao.delete(note)
            _events.emit(RoomLabEvent.Deleted(note))
        }
    }

    fun restore(note: NoteEntity) {
        launch {
            noteDao.insert(
                note.copy(
                    id = 0,
                    updatedAt = System.currentTimeMillis(),
                ),
            )
        }
    }
}
