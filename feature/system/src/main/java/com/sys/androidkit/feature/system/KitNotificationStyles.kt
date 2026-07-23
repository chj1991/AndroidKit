package com.sys.androidkit.feature.system

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.content.LocusIdCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat

object KitNotificationStyles {

    const val ID_PROGRESS = 2101
    const val ID_BIG_PICTURE = 2102
    const val ID_BUBBLE = 2103

    const val SHORTCUT_BUBBLE = "kit_bubble_shortcut"

    fun pendingFlags(): Int {
        return PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    }

    fun createDemoPicture(width: Int = 1200, height: Int = 600): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.parseColor("#1565C0"))
        val bar = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#42A5F5") }
        canvas.drawRect(0f, height * 0.65f, width.toFloat(), height.toFloat(), bar)
        val title = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = height * 0.12f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("AndroidKit", width / 2f, height * 0.42f, title)
        val sub = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#E3F2FD")
            textSize = height * 0.06f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("BigPicture Style Demo", width / 2f, height * 0.55f, sub)
        return bitmap
    }

    fun progressBuilder(
        context: Context,
        progress: Int,
        indeterminate: Boolean,
        done: Boolean,
    ): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, KitNotificationChannels.CHANNEL_PROGRESS)
            .setSmallIcon(R.drawable.ic_notification_kit)
            .setContentTitle(if (done) "下载完成" else "正在下载样例文件")
            .setOnlyAlertOnce(true)
            .setOngoing(!done)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
        return if (done) {
            builder
                .setContentText("进度通知已结束 · 100%")
                .setProgress(0, 0, false)
                .setAutoCancel(true)
        } else {
            builder
                .setContentText(if (indeterminate) "准备中…" else "$progress%")
                .setProgress(100, progress.coerceIn(0, 100), indeterminate)
        }
    }

    fun bigPictureBuilder(context: Context): NotificationCompat.Builder {
        val picture = createDemoPicture()
        return NotificationCompat.Builder(context, KitNotificationChannels.CHANNEL_MEDIA)
            .setSmallIcon(R.drawable.ic_notification_kit)
            .setContentTitle("大图通知")
            .setContentText("展开查看 BigPicture")
            .setLargeIcon(picture)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(picture)
                    .bigLargeIcon(null as Bitmap?)
                    .setSummaryText("Canvas 生成的演示大图"),
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
    }

    /**
     * 气泡通知：需 Android 11+，并在系统设置中允许本应用气泡。
     * @return null 表示系统版本不支持
     */
    fun bubbleBuilder(context: Context, message: String): NotificationCompat.Builder? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return null

        val person = Person.Builder()
            .setName("Kit Bot")
            .setImportant(true)
            .build()

        val bubbleIntent = Intent(context, BubbleActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            putExtra(BubbleActivity.EXTRA_MESSAGE, message)
        }
        val bubblePending = PendingIntent.getActivity(
            context,
            ID_BUBBLE,
            bubbleIntent,
            pendingFlags(),
        )

        val icon = IconCompat.createWithResource(context, R.drawable.ic_notification_kit)
        val shortcut = ShortcutInfoCompat.Builder(context, SHORTCUT_BUBBLE)
            .setShortLabel("Kit Bot")
            .setLongLabel("AndroidKit 气泡对话")
            .setIcon(icon)
            .setLongLived(true)
            .setPerson(person)
            .setLocusId(LocusIdCompat(SHORTCUT_BUBBLE))
            .setIntent(
                Intent(context, BubbleActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                },
            )
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)

        val bubbleMetadata = NotificationCompat.BubbleMetadata.Builder(bubblePending, icon)
            .setDesiredHeight(640)
            .setAutoExpandBubble(false)
            .setSuppressNotification(false)
            .build()

        return NotificationCompat.Builder(context, KitNotificationChannels.CHANNEL_BUBBLE)
            .setSmallIcon(R.drawable.ic_notification_kit)
            .setContentTitle("Kit Bot")
            .setContentText(message)
            .setShortcutId(SHORTCUT_BUBBLE)
            .setLocusId(LocusIdCompat(SHORTCUT_BUBBLE))
            .addPerson(person)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setBubbleMetadata(bubbleMetadata)
            .setStyle(
                NotificationCompat.MessagingStyle(person)
                    .addMessage(message, System.currentTimeMillis(), person),
            )
            .setAutoCancel(true)
    }
}
