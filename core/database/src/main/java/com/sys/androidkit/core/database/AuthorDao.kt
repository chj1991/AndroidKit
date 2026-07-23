package com.sys.androidkit.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthorDao {
    @Query("SELECT * FROM authors ORDER BY id ASC")
    fun observeAuthors(): Flow<List<AuthorEntity>>

    @Query("SELECT * FROM authors ORDER BY id ASC")
    suspend fun getAll(): List<AuthorEntity>

    @Transaction
    @Query("SELECT * FROM authors ORDER BY id ASC")
    fun observeAuthorsWithNotes(): Flow<List<AuthorWithNotes>>

    @Transaction
    @Query("SELECT * FROM authors WHERE id = :authorId LIMIT 1")
    fun observeAuthorWithNotes(authorId: Long): Flow<AuthorWithNotes?>

    @Query("SELECT * FROM authors WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): AuthorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(author: AuthorEntity): Long

    @Update
    suspend fun update(author: AuthorEntity)

    @Delete
    suspend fun delete(author: AuthorEntity)

    @Query("DELETE FROM authors WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM authors")
    suspend fun count(): Int
}
