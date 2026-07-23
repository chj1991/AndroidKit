package com.sys.androidkit.feature.system

import android.Manifest
import android.app.NotificationManager
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
            postBasic(
                channelId = KitNotificationChannels.CHANNEL_DEFAULT,
                title = "默认渠道",
                text = "IMPORTANCE_DEFAULT",
            )
        }
        binding.btnNotifyImportant.setOnClickListener {
            postBasic(
                channelId = KitNotificationChannels.CHANNEL_IMPORTANT,
                title = "重要渠道",
                text = "IMPORTANCE_HIGH",
            )
        }
        binding.btnNotifySilent.setOnClickListener {
            postBasic(
                channelId = KitNotificationChannels.CHANNEL_SILENT,
                title = "静默渠道",
                text = "IMPORTANCE_LOW",
            )
        }
        binding.btnProgressStart.setOnClickListener {
            if (!ensureCanNotify()) return@setOnClickListener
            KitNotificationChannels.ensureChannels(requireContext())
            viewModel.onChannelsEnsured()
            viewModel.startProgressDemo()
        }
        binding.btnProgressCancel.setOnClickListener {
            viewModel.cancelProgressDemo()
            NotificationManagerCompat.from(requireContext())
                .cancel(KitNotificationStyles.ID_PROGRESS)
        }
        binding.btnBigPicture.setOnClickListener { postBigPicture() }
        binding.btnBubble.setOnClickListener { postBubble() }
        binding.btnOpenChannelSettings.setOnClickListener { openChannelSettings() }
        binding.btnOpenBubbleSettings.setOnClickListener { openBubbleSettings() }
        refreshPermission()
    }

    override fun onResume() {
        super.onResume()
        refreshPermission()
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        binding.tvStatus.text = buildString {
                            append(state.message)
                            append("\n权限：")
                            append(if (state.permissionGranted) "已授予" else "未授予/不适用")
                            append(" · 渠道：")
                            append(if (state.channelsReady) "已创建" else "未确认")
                            if (state.progressRunning) append(" · 进度演示中")
                        }
                        binding.btnProgressStart.isEnabled = !state.progressRunning
                        binding.btnProgressCancel.isEnabled = state.progressRunning
                    }
                }
                launch {
                    viewModel.progressTicks.collect { tick ->
                        if (!ensureCanNotify(silent = true)) return@collect
                        val notification = when (tick) {
                            is ProgressTick.Running -> KitNotificationStyles.progressBuilder(
                                context = requireContext(),
                                progress = tick.percent,
                                indeterminate = tick.indeterminate,
                                done = false,
                            ).build()
                            ProgressTick.Done -> KitNotificationStyles.progressBuilder(
                                context = requireContext(),
                                progress = 100,
                                indeterminate = false,
                                done = true,
                            ).build()
                        }
                        NotificationManagerCompat.from(requireContext())
                            .notify(KitNotificationStyles.ID_PROGRESS, notification)
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

    private fun ensureCanNotify(silent: Boolean = false): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                if (!silent) {
                    viewModel.onError("请先在 Permission Lab 授予通知权限")
                }
                return false
            }
        }
        return true
    }

    private fun postBasic(channelId: String, title: String, text: String) {
        if (!ensureCanNotify()) return
        KitNotificationChannels.ensureChannels(requireContext())
        val notification = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.ic_notification_kit)
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

    private fun postBigPicture() {
        if (!ensureCanNotify()) return
        KitNotificationChannels.ensureChannels(requireContext())
        val notification = KitNotificationStyles.bigPictureBuilder(requireContext()).build()
        NotificationManagerCompat.from(requireContext())
            .notify(KitNotificationStyles.ID_BIG_PICTURE, notification)
        viewModel.onPosted("大图通知（BigPicture）")
        viewModel.onChannelsEnsured()
    }

    private fun postBubble() {
        if (!ensureCanNotify()) return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            viewModel.onError("气泡需要 Android 11（API 30）及以上")
            return
        }
        KitNotificationChannels.ensureChannels(requireContext())
        if (!areBubblesAllowed()) {
            viewModel.onError("系统未允许本应用气泡，请点「气泡设置」开启后重试")
            return
        }
        val message = "你好，这是 AndroidKit 气泡演示 @ ${System.currentTimeMillis() % 100_000}"
        val builder = KitNotificationStyles.bubbleBuilder(requireContext(), message)
        if (builder == null) {
            viewModel.onError("当前系统不支持 BubbleMetadata")
            return
        }
        NotificationManagerCompat.from(requireContext())
            .notify(KitNotificationStyles.ID_BUBBLE, builder.build())
        viewModel.onPosted("气泡通知（可下拉通知后点气泡图标展开）")
        viewModel.onChannelsEnsured()
    }

    private fun areBubblesAllowed(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return false
        val manager = requireContext().getSystemService(NotificationManager::class.java)
            ?: return false
        return manager.areBubblesAllowed()
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

    private fun openBubbleSettings() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent(Settings.ACTION_APP_NOTIFICATION_BUBBLE_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            }
        } else {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            }
        }
        startActivity(intent)
    }
}
