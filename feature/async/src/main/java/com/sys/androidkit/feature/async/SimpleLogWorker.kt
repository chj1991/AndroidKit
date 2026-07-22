package com.sys.androidkit.feature.async

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay

/**
 * 一次性后台任务 Demo：模拟 IO，再把结果写入 OutputData。
 */
class SimpleLogWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        delay(WORK_MS)
        val message = "Worker 完成 @ ${System.currentTimeMillis()}"
        return Result.success(workDataOf(KEY_MESSAGE to message))
    }

    companion object {
        const val UNIQUE_NAME = "androidkit_simple_log_work"
        const val KEY_MESSAGE = "message"
        private const val WORK_MS = 2_000L
    }
}
