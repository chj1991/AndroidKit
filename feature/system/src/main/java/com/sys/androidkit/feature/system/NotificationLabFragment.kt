package com.sys.androidkit.feature.system

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.system.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationLabFragment : BaseFragment<FragmentNotificationBinding>() {

    private val viewModel: NotificationLabViewModel by viewModels()
    private var notifyId = 1000

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentNotificationBinding = FragmentNotificationBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        binding.btnEnsureChannels.setOnClickListener {
            KitNotificationChannels.ensureChannels(requireContext())
            viewModel.onChannelsEnsured()
        }
        binding.btnNotifyDefault.setOnClickListener {
            postNotification(
                channelId = KitNotificationChannels.CHANNEL_DEFAULT,
                title = "默认渠道",
                text = "IMPORTANCE_DEFAULT",
            )
        }
        binding.btnNotifyImportant.setOnClickListener {
            postNotification(
                channelId = KitNotificationChannels.CHANNEL_IMPORTANT,
                title = "重要渠道",
                text = "IMPORTANCE_HIGH",
            )
        }
        binding.btnNotifySilent.setOnClickListener {
            postNotification(
                channelId = KitNotificationChannels.CHANNEL_SILENT,
                title = "静默渠道",
                text = "IMPORTANCE_LOW",
            )
        }
        binding.btnOpenChannelSettings.setOnClickListener { openChannelSettings() }
        refreshPermission()
    }

    override fun onResume() {
        super.onResume()
        refreshPermission()
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvStatus.text = buildString {
                        append(state.message)
                        append("\n权限：")
                        append(if (state.permissionGranted) "已授予" else "未授予/不适用")
                        append(" · 渠道：")
                        append(if (state.channelsReady) "已创建" else "未确认")
                    }
                }
            }
        }
    }

    private fun refreshPermission() {
        val granted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()
        }
        viewModel.updatePermission(granted)
    }

    private fun postNotification(channelId: String, title: String, text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                viewModel.onError("请先在 Permission Lab 授予通知权限")
                return
            }
        }
        KitNotificationChannels.ensureChannels(requireContext())
        val notification = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(
                when (channelId) {
                    KitNotificationChannels.CHANNEL_IMPORTANT -> NotificationCompat.PRIORITY_HIGH
                    KitNotificationChannels.CHANNEL_SILENT -> NotificationCompat.PRIORITY_LOW
                    else -> NotificationCompat.PRIORITY_DEFAULT
                },
            )
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(requireContext()).notify(notifyId++, notification)
        viewModel.onPosted(title)
        viewModel.onChannelsEnsured()
    }

    private fun openChannelSettings() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                putExtra(Settings.EXTRA_CHANNEL_ID, KitNotificationChannels.CHANNEL_DEFAULT)
            }
        } else {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = "package:${requireContext().packageName}".toUri()
            }
        }
        startActivity(intent)
    }
}
