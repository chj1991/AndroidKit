package com.sys.androidkit.feature.storage

import androidx.lifecycle.viewModelScope
import com.sys.androidkit.core.database.AuthorDao
import com.sys.androidkit.core.database.AuthorEntity
import com.sys.androidkit.core.database.AuthorWithNotes
import com.sys.androidkit.core.database.NoteDao
import com.sys.androidkit.core.database.NoteEntity
import com.sys.androidkit.core.ui.base.BaseViewModel
import com.sys.androidkit.core.ui.widget.DemoState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class RelationUiState(
    val rows: List<AuthorWithNotes> = emptyList(),
    val demoState: DemoState = DemoState.Loading,
)

@HiltViewModel
class RelationLabViewModel @Inject constructor(
    private val authorDao: AuthorDao,
    private val noteDao: NoteDao,
) : BaseViewModel() {

    val uiState: StateFlow<RelationUiState> = authorDao.observeAuthorsWithNotes()
        .map { rows ->
            RelationUiState(
                rows = rows,
                demoState = if (rows.isEmpty()) {
                    DemoState.Empty("暂无作者，点「种子作者」或「新增作者」")
                } else {
                    DemoState.Content
                },
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            RelationUiState(),
        )

    fun ensureSeedAuthors() {
        launch {
            if (authorDao.count() == 0) {
                authorDao.insert(AuthorEntity(name = "Alice"))
                authorDao.insert(AuthorEntity(name = "Bob"))
            }
        }
    }

    fun addAuthor(name: String) {
        launch {
            val trimmed = name.trim().ifBlank { "Author ${authorDao.count() + 1}" }
            authorDao.insert(AuthorEntity(name = trimmed))
        }
    }

    fun renameAuthor(author: AuthorEntity, name: String) {
        launch {
            val trimmed = name.trim()
            if (trimmed.isEmpty()) return@launch
            authorDao.update(author.copy(name = trimmed))
        }
    }

    fun deleteAuthor(author: AuthorEntity) {
        launch {
            // 演示：先解除关联（authorId=0），再删作者
            noteDao.getByAuthor(author.id).forEach { note ->
                noteDao.update(
                    note.copy(authorId = 0L, updatedAt = System.currentTimeMillis()),
                )
            }
            authorDao.delete(author)
        }
    }

    fun addNoteFor(author: AuthorEntity) {
        launch {
            val existing = noteDao.getByAuthor(author.id).size
            val now = System.currentTimeMillis()
            noteDao.insert(
                NoteEntity(
                    title = "${author.name} #${existing + 1}",
                    content = "@Relation 一对多 · authorId=${author.id}",
                    tags = listOf("relation", author.name.lowercase()),
                    authorId = author.id,
                    createdAt = now,
                    updatedAt = now,
                ),
            )
        }
    }

    fun addNoteRoundRobin() {
        launch {
            val rows = uiState.value.rows
            if (rows.isEmpty()) {
                ensureSeedAuthors()
                return@launch
            }
            val target = rows.minByOrNull { it.notes.size } ?: rows.first()
            addNoteFor(target.author)
        }
    }

    fun deleteLastNoteOf(authorId: Long) {
        launch {
            val note = noteDao.getByAuthor(authorId).maxByOrNull { it.updatedAt } ?: return@launch
            noteDao.delete(note)
        }
    }
}
