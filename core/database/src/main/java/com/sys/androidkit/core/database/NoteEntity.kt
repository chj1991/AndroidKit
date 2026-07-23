package com.sys.androidkit.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    /** TypeConverter：逗号分隔存储 */
    val tags: List<String> = emptyList(),
    /** DB-05：一对多外键，0 表示未归属作者 */
    val authorId: Long = 0L,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)
