package com.sys.androidkit.core.database

import androidx.room.TypeConverter

/** DB-04：`List<String>` ↔ 逗号分隔 TEXT */
class Converters {

    @TypeConverter
    fun fromTags(tags: List<String>): String = tags.joinToString(",")

    @TypeConverter
    fun toTags(value: String): List<String> =
        value.split(',')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
}
