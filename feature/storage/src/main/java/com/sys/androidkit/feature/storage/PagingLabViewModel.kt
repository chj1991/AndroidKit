package com.sys.androidkit.feature.storage

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sys.androidkit.core.database.AuthorDao
import com.sys.androidkit.core.database.AuthorEntity
import com.sys.androidkit.core.database.NoteDao
import com.sys.androidkit.core.database.NoteEntity
import com.sys.androidkit.core.database.NoteWithAuthor
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PagingLabMeta(
    val totalNotes: Int = 0,
    val message: String = "可种子大量数据后滚动加载",
)

@HiltViewModel
class PagingLabViewModel @Inject constructor(
    private val noteDao: NoteDao,
    private val authorDao: AuthorDao,
) : BaseViewModel() {

    private val _meta = MutableStateFlow(PagingLabMeta())
    val meta: StateFlow<PagingLabMeta> = _meta.asStateFlow()

    val pagingFlow: Flow<PagingData<NoteWithAuthor>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = 10,
            enablePlaceholders = false,
            initialLoadSize = PAGE_SIZE * 2,
        ),
        pagingSourceFactory = { noteDao.pagingNotesWithAuthor() },
    ).flow.cachedIn(viewModelScope)

    init {
        refreshMeta()
    }

    fun refreshMeta() {
        launch {
            _meta.value = PagingLabMeta(
                totalNotes = noteDao.count(),
                message = "pageSize=$PAGE_SIZE · Room PagingSource 自动失效刷新",
            )
        }
    }

    fun seedBulk(count: Int = SEED_COUNT) {
        launch {
            var authors = authorDao.getAll()
            if (authors.isEmpty()) {
                authorDao.insert(AuthorEntity(name = "Alice"))
                authorDao.insert(AuthorEntity(name = "Bob"))
                authorDao.insert(AuthorEntity(name = "Carol"))
                authors = authorDao.getAll()
            }
            val ids = authors.map { it.id }
            val start = noteDao.count()
            val now = System.currentTimeMillis()
            val batch = List(count) { index ->
                val n = start + index + 1
                NoteEntity(
                    title = "Paged Note #$n",
                    content = "Paging3 演示条目 · index=$n",
                    tags = listOf("paging", "room"),
                    authorId = ids[index % ids.size],
                    createdAt = now - index * 1_000L,
                    updatedAt = now - index * 1_000L,
                )
            }
            noteDao.insertAll(batch)
            _meta.value = PagingLabMeta(
                totalNotes = noteDao.count(),
                message = "已种子 +$count 条，请向下滚动",
            )
        }
    }

    fun clearNotes() {
        launch {
            noteDao.deleteAll()
            _meta.value = PagingLabMeta(
                totalNotes = 0,
                message = "已清空 notes 表",
            )
        }
    }

    companion object {
        const val PAGE_SIZE = 20
        const val SEED_COUNT = 80
    }
}
