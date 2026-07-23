package com.sys.androidkit.feature.system

import androidx.lifecycle.viewModelScope
import com.sys.androidkit.core.datastore.AppPreferences
import com.sys.androidkit.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class BiometricUiState(
    val loggedIn: Boolean = false,
    val username: String = "",
    val biometricEnabled: Boolean = false,
    val authMode: BiometricAuthMode = BiometricAuthMode.WEAK_OR_CREDENTIAL,
    val capabilityReport: String = "检测中…",
    val statusMessage: String = "演示账号 kit / 1234，登录后可开启生物识别",
    val lastAuthType: String? = null,
)

@HiltViewModel
class BiometricLabViewModel @Inject constructor(
    private val prefs: AppPreferences,
) : BaseViewModel() {

    private val session = MutableStateFlow(Session())
    private val capabilityReport = MutableStateFlow("检测中…")

    val uiState: StateFlow<BiometricUiState> = combine(
        session,
        prefs.biometricLoginEnabled,
        capabilityReport,
    ) { sess, bioEnabled, report ->
        BiometricUiState(
            loggedIn = sess.loggedIn,
            username = sess.username,
            biometricEnabled = bioEnabled,
            authMode = sess.authMode,
            capabilityReport = report,
            statusMessage = sess.statusMessage,
            lastAuthType = sess.lastAuthType,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BiometricUiState(),
    )

    fun refreshCapability(report: String) {
        capabilityReport.value = report
    }

    fun setAuthMode(mode: BiometricAuthMode) {
        session.update { it.copy(authMode = mode) }
    }

    fun loginWithPassword(username: String, password: String) {
        val user = username.trim()
        if (user == DEMO_USER && password == DEMO_PASSWORD) {
            session.update {
                it.copy(
                    loggedIn = true,
                    username = user,
                    statusMessage = "密码登录成功。可开启生物识别，下次用指纹/面部解锁。",
                    lastAuthType = "Password",
                )
            }
        } else {
            session.update {
                it.copy(statusMessage = "账号或密码错误（演示：kit / 1234）")
            }
        }
    }

    fun onBiometricSuccess(authenticationTypeLabel: String) {
        session.update {
            it.copy(
                loggedIn = true,
                username = DEMO_USER,
                statusMessage = "生物识别登录成功（$authenticationTypeLabel）",
                lastAuthType = authenticationTypeLabel,
            )
        }
    }

    fun onBiometricError(message: String) {
        session.update { it.copy(statusMessage = message) }
    }

    fun onBiometricFailed() {
        session.update {
            it.copy(statusMessage = "生物特征不匹配，可重试或改用密码登录")
        }
    }

    fun logout() {
        session.update {
            it.copy(
                loggedIn = false,
                username = "",
                statusMessage = if (uiState.value.biometricEnabled) {
                    "已退出。可使用生物识别快速登录，或再输密码。"
                } else {
                    "已退出。请使用密码登录。"
                },
                lastAuthType = null,
            )
        }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        launch {
            if (enabled && !uiState.value.loggedIn) {
                session.update {
                    it.copy(statusMessage = "请先密码登录，再开启生物识别")
                }
                return@launch
            }
            prefs.setBiometricLoginEnabled(enabled)
            session.update {
                it.copy(
                    statusMessage = if (enabled) {
                        "已开启生物识别登录（偏好已写入 DataStore）"
                    } else {
                        "已关闭生物识别登录"
                    },
                )
            }
        }
    }

    private data class Session(
        val loggedIn: Boolean = false,
        val username: String = "",
        val authMode: BiometricAuthMode = BiometricAuthMode.WEAK_OR_CREDENTIAL,
        val statusMessage: String = "演示账号 kit / 1234，登录后可开启生物识别",
        val lastAuthType: String? = null,
    )

    companion object {
        const val DEMO_USER = "kit"
        const val DEMO_PASSWORD = "1234"
    }
}
