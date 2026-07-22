package com.sys.androidkit.feature.system

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object KitNotificationChannels {
    const val CHANNEL_DEFAULT = "kit_default"
    const val CHANNEL_IMPORTANT = "kit_important"
    const val CHANNEL_SILENT = "kit_silent"

    fun ensureChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        val channels = listOf(
            NotificationChannel(
                CHANNEL_DEFAULT,
                "默认通知",
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = "普通提醒，有声音"
            },
            NotificationChannel(
                CHANNEL_IMPORTANT,
                "重要通知",
                NotificationManager.IMPORTANCE_HIGH,
            ).apply {
                description = "高优先级，可弹出抬头"
            },
            NotificationChannel(
                CHANNEL_SILENT,
                "静默通知",
                NotificationManager.IMPORTANCE_LOW,
            ).apply {
                description = "无声音，状态栏可见"
            },
        )
        manager.createNotificationChannels(channels)
    }
}
