package com.sys.androidkit.core.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/** DB-03：v1 → v2 增加 tags / updatedAt */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE notes ADD COLUMN tags TEXT NOT NULL DEFAULT ''",
        )
        db.execSQL(
            "ALTER TABLE notes ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0",
        )
        db.execSQL(
            "UPDATE notes SET updatedAt = createdAt WHERE updatedAt = 0",
        )
    }
}

/** DB-05：v2 → v3 authors 表 + notes.authorId */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS authors (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL
            )
            """.trimIndent(),
        )
        db.execSQL(
            "ALTER TABLE notes ADD COLUMN authorId INTEGER NOT NULL DEFAULT 0",
        )
    }
}
