package com.sys.androidkit.feature.system

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricPrompt

enum class BiometricAuthMode(
    val authenticators: Int,
    val label: String,
) {
    STRONG(Authenticators.BIOMETRIC_STRONG, "Strong（指纹/3D 面容）"),
    WEAK(Authenticators.BIOMETRIC_WEAK, "Weak（含 2D 面容）"),
    STRONG_OR_CREDENTIAL(
        Authenticators.BIOMETRIC_STRONG or Authenticators.DEVICE_CREDENTIAL,
        "Strong + 锁屏凭据",
    ),
    WEAK_OR_CREDENTIAL(
        Authenticators.BIOMETRIC_WEAK or Authenticators.DEVICE_CREDENTIAL,
        "Weak + 锁屏凭据",
    ),
}

object BiometricAuthHelper {

    fun statusLine(context: Context, authenticators: Int): String {
        val code = BiometricManager.from(context).canAuthenticate(authenticators)
        return when (code) {
            BiometricManager.BIOMETRIC_SUCCESS -> "可用"
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> "未录入生物特征"
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "无硬件"
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> "硬件暂不可用"
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> "需安全更新"
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> "不支持该 authenticator 组合"
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> "状态未知"
            else -> "code=$code"
        }
    }

    fun canAuthenticate(context: Context, authenticators: Int): Boolean {
        return BiometricManager.from(context).canAuthenticate(authenticators) ==
            BiometricManager.BIOMETRIC_SUCCESS
    }

    fun capabilityReport(context: Context): String = buildString {
        appendLine("Strong: ${statusLine(context, Authenticators.BIOMETRIC_STRONG)}")
        appendLine("Weak: ${statusLine(context, Authenticators.BIOMETRIC_WEAK)}")
        append(
            "DeviceCredential: ${statusLine(context, Authenticators.DEVICE_CREDENTIAL)}",
        )
    }

    fun buildPromptInfo(
        mode: BiometricAuthMode,
        title: String,
        subtitle: String,
        negativeButton: String,
    ): BiometricPrompt.PromptInfo {
        val builder = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setAllowedAuthenticators(mode.authenticators)
            .setConfirmationRequired(true)
        val allowsDeviceCredential =
            mode.authenticators and Authenticators.DEVICE_CREDENTIAL != 0
        // 含 DEVICE_CREDENTIAL 时系统禁止再设 negativeButton
        if (!allowsDeviceCredential) {
            builder.setNegativeButtonText(negativeButton)
        }
        return builder.build()
    }
}
