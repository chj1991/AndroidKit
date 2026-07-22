package com.sys.androidkit.feature.system

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimerForegroundService : Service() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var tickJob: Job? = null
    private var seconds = 0

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> {
                stopSelfSafely()
                return START_NOT_STICKY
            }
            else -> startTimer()
        }
        return START_STICKY
    }

    private fun startTimer() {
        KitNotificationChannels.ensureChannels(this)
        val notification = buildNotification(seconds)
        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            notification,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            } else {
                0
            },
        )
        ForegroundTimerBus.markRunning(true)
        if (tickJob?.isActive == true) return
        tickJob = scope.launch {
            while (isActive) {
                delay(1000)
                seconds += 1
                ForegroundTimerBus.tick(seconds)
                val manager = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
                manager.notify(NOTIFICATION_ID, buildNotification(seconds))
            }
        }
    }

    private fun buildNotification(seconds: Int) =
        NotificationCompat.Builder(this, KitNotificationChannels.CHANNEL_DEFAULT)
            .setContentTitle("前台计时服务")
            .setContentText("已运行 ${seconds}s")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

    private fun stopSelfSafely() {
        tickJob?.cancel()
        tickJob = null
        ForegroundTimerBus.markRunning(false)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        tickJob?.cancel()
        ForegroundTimerBus.markRunning(false)
        super.onDestroy()
    }

    companion object {
        const val ACTION_STOP = "com.sys.androidkit.feature.system.STOP_TIMER"
        private const val NOTIFICATION_ID = 2001
    }
}
