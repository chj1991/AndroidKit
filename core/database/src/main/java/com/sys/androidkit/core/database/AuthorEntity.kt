package com.sys.androidkit.core.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "authors")
data class AuthorEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
)

/** Author 1 — N Note */
data class AuthorWithNotes(
    @Embedded val author: AuthorEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "authorId",
    )
    val notes: List<NoteEntity>,
)

/** Note N — 1 Author（authorId=0 时 author 为 null） */
data class NoteWithAuthor(
    @Embedded val note: NoteEntity,
    @Relation(
        parentColumn = "authorId",
        entityColumn = "id",
    )
    val author: AuthorEntity?,
)
