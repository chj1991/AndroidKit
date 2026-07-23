package com.sys.androidkit.core.common.time

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/** COM-05：Demo 共用时间格式化 */
object DateFormats {

    private val timeHms = ThreadLocal.withInitial {
        SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    }
    private val timeHmsMillis = ThreadLocal.withInitial {
        SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
    }
    private val dateTime = ThreadLocal.withInitial {
        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    }

    fun formatTime(millis: Long = System.currentTimeMillis()): String =
        timeHms.get()!!.format(Date(millis))

    fun formatTimeMillis(millis: Long = System.currentTimeMillis()): String =
        timeHmsMillis.get()!!.format(Date(millis))

    fun formatDateTime(millis: Long = System.currentTimeMillis()): String =
        dateTime.get()!!.format(Date(millis))

    fun relativeToNow(millis: Long, now: Long = System.currentTimeMillis()): String {
        val delta = now - millis
        if (delta < 0) return formatDateTime(millis)
        return when {
            delta < TimeUnit.SECONDS.toMillis(10) -> "刚刚"
            delta < TimeUnit.MINUTES.toMillis(1) -> "${delta / 1000} 秒前"
            delta < TimeUnit.HOURS.toMillis(1) -> "${delta / TimeUnit.MINUTES.toMillis(1)} 分钟前"
            delta < TimeUnit.DAYS.toMillis(1) -> "${delta / TimeUnit.HOURS.toMillis(1)} 小时前"
            delta < TimeUnit.DAYS.toMillis(7) -> "${delta / TimeUnit.DAYS.toMillis(1)} 天前"
            else -> formatDateTime(millis)
        }
    }
}
