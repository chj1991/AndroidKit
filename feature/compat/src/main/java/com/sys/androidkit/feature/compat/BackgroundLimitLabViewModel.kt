package com.sys.androidkit.feature.compat

import android.content.Context
import android.os.Build
import android.os.PowerManager
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class BackgroundLimitUiState(
    val report: String = "",
    val ignoringBatteryOptimizations: Boolean = false,
)

@HiltViewModel
class BackgroundLimitLabViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(BackgroundLimitUiState())
    val uiState: StateFlow<BackgroundLimitUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val pm = context.getSystemService(PowerManager::class.java)
        val ignoring = pm?.isIgnoringBatteryOptimizations(context.packageName) == true
        val canScheduleExact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val am = context.getSystemService(android.app.AlarmManager::class.java)
            am?.canScheduleExactAlarms() == true
        } else {
            true
        }
        val report = buildString {
            appendLine("SDK=${Build.VERSION.SDK_INT}")
            appendLine("忽略电池优化=$ignoring")
            appendLine("可调度精确闹钟=$canScheduleExact")
            appendLine()
            appendLine("常见后台限制：")
            appendLine("1. 后台启动 Activity（Android 10+）受限")
            appendLine("2. 后台定位 / 前台服务类型声明（Android 8/10/14）")
            appendLine("3. 精确闹钟需 SCHEDULE_EXACT_ALARM / USE_EXACT_ALARM")
            appendLine("4. 电池优化会推迟 Job/Alarm/推送唤醒")
            appendLine()
            appendLine("实践：能用 FGS/WorkManager 就别硬后台起页；通知点进前台再导航。")
        }
        _uiState.update {
            it.copy(report = report, ignoringBatteryOptimizations = ignoring)
        }
    }
}
