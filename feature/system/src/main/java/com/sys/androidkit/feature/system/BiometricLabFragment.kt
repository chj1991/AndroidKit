package com.sys.androidkit.feature.system

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.system.databinding.FragmentBiometricBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BiometricLabFragment : BaseFragment<FragmentBiometricBinding>() {

    private val viewModel: BiometricLabViewModel by viewModels()
    private var updatingSwitch = false

    private val biometricPrompt by lazy {
        BiometricPrompt(
            this,
            ContextCompat.getMainExecutor(requireContext()),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    viewModel.onBiometricSuccess(authTypeLabel(result.authenticationType))
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    if (errorCode == BiometricPrompt.ERROR_USER_CANCELED ||
                        errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                        errorCode == BiometricPrompt.ERROR_CANCELED
                    ) {
                        viewModel.onBiometricError("已取消生物识别（$errString）")
                    } else {
                        viewModel.onBiometricError("错误 $errorCode：$errString")
                    }
                }

                override fun onAuthenticationFailed() {
                    viewModel.onBiometricFailed()
                }
            },
        )
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentBiometricBinding = FragmentBiometricBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        binding.btnRefreshCapability.setOnClickListener { refreshCapability() }
        binding.btnEnroll.setOnClickListener { openBiometricEnroll() }

        binding.chipStrong.setOnClickListener { viewModel.setAuthMode(BiometricAuthMode.STRONG) }
        binding.chipWeak.setOnClickListener { viewModel.setAuthMode(BiometricAuthMode.WEAK) }
        binding.chipStrongCred.setOnClickListener {
            viewModel.setAuthMode(BiometricAuthMode.STRONG_OR_CREDENTIAL)
        }
        binding.chipWeakCred.setOnClickListener {
            viewModel.setAuthMode(BiometricAuthMode.WEAK_OR_CREDENTIAL)
        }

        binding.btnPasswordLogin.setOnClickListener {
            viewModel.loginWithPassword(
                binding.etUsername.text?.toString().orEmpty(),
                binding.etPassword.text?.toString().orEmpty(),
            )
        }
        binding.btnBiometricLogin.setOnClickListener { startBiometricLogin() }
        binding.btnLogout.setOnClickListener { viewModel.logout() }
        binding.switchBiometric.setOnCheckedChangeListener { _, checked ->
            if (!updatingSwitch) {
                viewModel.setBiometricEnabled(checked)
            }
        }

        refreshCapability()
    }

    override fun onResume() {
        super.onResume()
        refreshCapability()
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvCapability.text = state.capabilityReport
                    binding.tvStatus.text = state.statusMessage
                    binding.tvSession.text = if (state.loggedIn) {
                        getString(
                            R.string.feature_system_biometric_session_in,
                            state.username,
                            state.lastAuthType ?: "-",
                        )
                    } else {
                        getString(R.string.feature_system_biometric_session_out)
                    }

                    binding.chipStrong.isChecked = state.authMode == BiometricAuthMode.STRONG
                    binding.chipWeak.isChecked = state.authMode == BiometricAuthMode.WEAK
                    binding.chipStrongCred.isChecked =
                        state.authMode == BiometricAuthMode.STRONG_OR_CREDENTIAL
                    binding.chipWeakCred.isChecked =
                        state.authMode == BiometricAuthMode.WEAK_OR_CREDENTIAL

                    binding.panelLogin.isVisible = !state.loggedIn
                    binding.panelLoggedIn.isVisible = state.loggedIn
                    binding.btnBiometricLogin.isEnabled = state.biometricEnabled
                    binding.btnBiometricLogin.alpha = if (state.biometricEnabled) 1f else 0.5f

                    updatingSwitch = true
                    binding.switchBiometric.isChecked = state.biometricEnabled
                    updatingSwitch = false
                }
            }
        }
    }

    private fun refreshCapability() {
        viewModel.refreshCapability(BiometricAuthHelper.capabilityReport(requireContext()))
    }

    private fun startBiometricLogin() {
        val state = viewModel.uiState.value
        if (!state.biometricEnabled) {
            viewModel.onBiometricError("请先密码登录并开启「生物识别登录」开关")
            return
        }
        val mode = state.authMode
        if (!BiometricAuthHelper.canAuthenticate(requireContext(), mode.authenticators)) {
            viewModel.onBiometricError(
                "当前模式不可用：${BiometricAuthHelper.statusLine(requireContext(), mode.authenticators)}。" +
                    "可点「去系统录入」或切换 authenticator。",
            )
            return
        }
        val promptInfo = BiometricAuthHelper.buildPromptInfo(
            mode = mode,
            title = getString(R.string.feature_system_biometric_prompt_title),
            subtitle = getString(R.string.feature_system_biometric_prompt_subtitle, mode.label),
            negativeButton = getString(R.string.feature_system_biometric_prompt_negative),
        )
        biometricPrompt.authenticate(promptInfo)
    }

    private fun openBiometricEnroll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    viewModel.uiState.value.authMode.authenticators,
                )
            }
            runCatching { startActivity(intent) }
                .onFailure {
                    startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
                }
        } else {
            startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
        }
    }

    private fun authTypeLabel(type: Int): String = when (type) {
        BiometricPrompt.AUTHENTICATION_RESULT_TYPE_BIOMETRIC -> "Biometric（指纹/面部）"
        BiometricPrompt.AUTHENTICATION_RESULT_TYPE_DEVICE_CREDENTIAL -> "DeviceCredential（PIN/图案）"
        else -> "Unknown($type)"
    }
}
