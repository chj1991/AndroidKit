package com.sys.androidkit.feature.image

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class GalleryImage(
    val id: Long,
    val uri: Uri,
    val displayName: String,
)

data class GalleryRow(
    val image: GalleryImage,
    val selected: Boolean,
    val order: Int?,
)

object GalleryMediaStore {

    suspend fun loadImages(context: Context, limit: Int = 200): List<GalleryImage> =
        withContext(Dispatchers.IO) {
            val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
            )
            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
            val result = ArrayList<GalleryImage>(limit)
            context.contentResolver.query(
                collection,
                projection,
                null,
                null,
                sortOrder,
            )?.use { cursor ->
                val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                while (cursor.moveToNext() && result.size < limit) {
                    val id = cursor.getLong(idCol)
                    val name = cursor.getString(nameCol).orEmpty()
                    val uri = ContentUris.withAppendedId(collection, id)
                    result += GalleryImage(id = id, uri = uri, displayName = name)
                }
            }
            result
        }
}
