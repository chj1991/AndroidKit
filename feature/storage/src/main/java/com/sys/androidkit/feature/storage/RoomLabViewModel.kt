package com.sys.androidkit.feature.storage

import androidx.lifecycle.viewModelScope
import com.sys.androidkit.core.database.AppDatabase
import com.sys.androidkit.core.database.NoteDao
import com.sys.androidkit.core.database.NoteEntity
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

sealed interface RoomLabEvent {
    data class Added(val title: String) : RoomLabEvent
    data class Updated(val title: String) : RoomLabEvent
    data class Deleted(val note: NoteEntity) : RoomLabEvent
    data object Cleared : RoomLabEvent
}

data class RoomLabUiState(
    val notes: List<NoteEntity> = emptyList(),
    val totalCount: Int = 0,
    val query: String = "",
)

@HiltViewModel
class RoomLabViewModel @Inject constructor(
    private val noteDao: NoteDao,
) : BaseViewModel() {

    val dbVersion: Int = AppDatabase.VERSION

    private val query = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<RoomLabUiState> = combine(
        query.flatMapLatest { q -> noteDao.observeNotes(q.trim()) },
        noteDao.observeCount(),
        query,
    ) { notes, count, q ->
        RoomLabUiState(notes = notes, totalCount = count, query = q)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), RoomLabUiState())

    private val _events = MutableSharedFlow<RoomLabEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<RoomLabEvent> = _events.asSharedFlow()

    fun onQueryChanged(value: String) {
        query.value = value
    }

    fun addNote(title: String, content: String, tagsCsv: String) {
        launch {
            val now = System.currentTimeMillis()
            val note = NoteEntity(
                title = title.ifBlank { "未命名笔记" },
                content = content.ifBlank { "（无内容）" },
                tags = parseTags(tagsCsv).ifEmpty { listOf("room") },
                createdAt = now,
                updatedAt = now,
            )
            noteDao.insert(note)
            _events.emit(RoomLabEvent.Added(note.title))
        }
    }

    fun updateNote(note: NoteEntity, title: String, content: String, tagsCsv: String) {
        launch {
            val updated = note.copy(
                title = title.ifBlank { note.title },
                content = content.ifBlank { note.content },
                tags = parseTags(tagsCsv),
                updatedAt = System.currentTimeMillis(),
            )
            noteDao.update(updated)
            _events.emit(RoomLabEvent.Updated(updated.title))
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

    fun clearAll() {
        launch {
            noteDao.deleteAll()
            _events.emit(RoomLabEvent.Cleared)
        }
    }

    private fun parseTags(csv: String): List<String> =
        csv.split(',', '，', ' ')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
}
