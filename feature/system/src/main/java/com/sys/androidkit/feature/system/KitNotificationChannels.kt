package com.sys.androidkit.feature.system

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object KitNotificationChannels {
    const val CHANNEL_DEFAULT = "kit_default"
    const val CHANNEL_IMPORTANT = "kit_important"
    const val CHANNEL_SILENT = "kit_silent"
    const val CHANNEL_PROGRESS = "kit_progress"
    const val CHANNEL_MEDIA = "kit_media"
    const val CHANNEL_BUBBLE = "kit_bubble"

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
            NotificationChannel(
                CHANNEL_PROGRESS,
                "进度通知",
                NotificationManager.IMPORTANCE_LOW,
            ).apply {
                description = "下载/上传进度，低打扰"
                setShowBadge(false)
            },
            NotificationChannel(
                CHANNEL_MEDIA,
                "媒体 / 大图",
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = "BigPicture 等富媒体样式"
            },
            NotificationChannel(
                CHANNEL_BUBBLE,
                "对话气泡",
                NotificationManager.IMPORTANCE_HIGH,
            ).apply {
                description = "Messaging + Bubble（Android 11+）"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    setAllowBubbles(true)
                }
            },
        )
        manager.createNotificationChannels(channels)
    }
}
