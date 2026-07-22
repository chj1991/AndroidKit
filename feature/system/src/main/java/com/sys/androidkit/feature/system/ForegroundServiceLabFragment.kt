package com.sys.androidkit.feature.system

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.system.databinding.FragmentForegroundServiceBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForegroundServiceLabFragment : BaseFragment<FragmentForegroundServiceBinding>() {

    private val viewModel: ForegroundServiceLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentForegroundServiceBinding =
        FragmentForegroundServiceBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnStart.setOnClickListener { startTimerService() }
        binding.btnStop.setOnClickListener { stopTimerService() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvStatus.text = if (state.running) {
                        getString(R.string.feature_system_fg_running, state.seconds)
                    } else {
                        getString(R.string.feature_system_fg_idle, state.seconds)
                    }
                }
            }
        }
    }

    private fun startTimerService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                showMessage(
                    messageRes = R.string.feature_system_fg_need_notification,
                    length = Snackbar.LENGTH_LONG,
                )
                return
            }
        }
        KitNotificationChannels.ensureChannels(requireContext())
        val intent = Intent(requireContext(), TimerForegroundService::class.java)
        ContextCompat.startForegroundService(requireContext(), intent)
    }

    private fun stopTimerService() {
        val intent = Intent(requireContext(), TimerForegroundService::class.java).apply {
            action = TimerForegroundService.ACTION_STOP
        }
        requireContext().startService(intent)
    }
}
