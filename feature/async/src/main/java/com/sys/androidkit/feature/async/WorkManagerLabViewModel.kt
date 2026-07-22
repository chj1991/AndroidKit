package com.sys.androidkit.feature.async

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class WorkManagerUiState(
    val status: String = "空闲：可入队一次性任务",
)

@HiltViewModel
class WorkManagerLabViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : BaseViewModel() {

    private val workManager = WorkManager.getInstance(context)

    private val _uiState = MutableStateFlow(WorkManagerUiState())
    val uiState: StateFlow<WorkManagerUiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null

    fun enqueue() {
        val request = OneTimeWorkRequestBuilder<SimpleLogWorker>().build()
        workManager.enqueueUniqueWork(
            SimpleLogWorker.UNIQUE_NAME,
            ExistingWorkPolicy.REPLACE,
            request,
        )
        observe(request.id)
        _uiState.value = WorkManagerUiState("已入队（REPLACE）…")
    }

    fun cancel() {
        workManager.cancelUniqueWork(SimpleLogWorker.UNIQUE_NAME)
        _uiState.value = WorkManagerUiState("已请求取消 unique work")
    }

    private fun observe(id: java.util.UUID) {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            workManager.getWorkInfoByIdFlow(id).collectLatest { info ->
                _uiState.value = WorkManagerUiState(format(info))
            }
        }
    }

    private fun format(info: WorkInfo?): String {
        if (info == null) return "无 WorkInfo（可能已清除）"
        val output = info.outputData.getString(SimpleLogWorker.KEY_MESSAGE).orEmpty()
        return buildString {
            append("state=${info.state}")
            append("\nrunAttemptCount=${info.runAttemptCount}")
            if (output.isNotEmpty()) append("\n$output")
            when (info.state) {
                WorkInfo.State.ENQUEUED -> append("\n提示：进程被杀后仍可能继续执行")
                WorkInfo.State.RUNNING -> append("\nWorker 正在执行…")
                WorkInfo.State.SUCCEEDED -> append("\n一次性任务成功")
                WorkInfo.State.FAILED -> append("\n任务失败")
                WorkInfo.State.CANCELLED -> append("\n任务已取消")
                WorkInfo.State.BLOCKED -> append("\n被前置约束阻塞")
            }
        }
    }
}
