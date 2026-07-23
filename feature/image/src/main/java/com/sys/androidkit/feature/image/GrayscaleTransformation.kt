package com.sys.androidkit.feature.image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import coil.size.Size
import coil.transform.Transformation

/**
 * 自定义 Coil Transformation：饱和度置 0，得到灰度图。
 * cacheKey 参与 Coil 内存/磁盘缓存键，避免与原图互相覆盖。
 */
class GrayscaleTransformation : Transformation {

    override val cacheKey: String = javaClass.name

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
            colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
        }
        val config = input.config ?: Bitmap.Config.ARGB_8888
        val output = Bitmap.createBitmap(input.width, input.height, config)
        Canvas(output).drawBitmap(input, 0f, 0f, paint)
        return output
    }
}
