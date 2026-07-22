package com.sys.androidkit.feature.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.system.databinding.FragmentBroadcastBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BroadcastLabFragment : BaseFragment<FragmentBroadcastBinding>() {

    private val viewModel: BroadcastLabViewModel by viewModels()
    private var receiver: BroadcastReceiver? = null
    private var updatingUi = false

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentBroadcastBinding = FragmentBroadcastBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.switchRegister.setOnCheckedChangeListener { _, isChecked ->
            if (updatingUi) return@setOnCheckedChangeListener
            if (isChecked) registerReceiver() else unregisterReceiverSafely(updateState = true)
        }
        binding.btnSend.setOnClickListener { sendLocalBroadcast() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvStatus.text = if (state.registered) {
                        getString(R.string.feature_system_broadcast_status_on)
                    } else {
                        getString(R.string.feature_system_broadcast_status_off)
                    }
                    binding.tvLogs.text = state.logs.takeLast(20).joinToString("\n")
                    updatingUi = true
                    binding.switchRegister.isChecked = state.registered
                    updatingUi = false
                }
            }
        }
    }

    override fun onDestroyView() {
        unregisterReceiverSafely(updateState = false)
        super.onDestroyView()
    }

    private fun registerReceiver() {
        if (receiver != null) {
            viewModel.setRegistered(true)
            return
        }
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val payload = intent?.getStringExtra(EXTRA_PAYLOAD).orEmpty()
                viewModel.onReceived(payload.ifEmpty { intent?.action.orEmpty() })
            }
        }
        val filter = IntentFilter(ACTION_KIT_PING)
        ContextCompat.registerReceiver(
            requireContext(),
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
        viewModel.setRegistered(true)
    }

    private fun unregisterReceiverSafely(updateState: Boolean) {
        receiver?.let {
            runCatching { requireContext().unregisterReceiver(it) }
        }
        receiver = null
        if (updateState) {
            viewModel.setRegistered(false)
        }
    }

    private fun sendLocalBroadcast() {
        val payload = "ping@${System.currentTimeMillis() % 100000}"
        val intent = Intent(ACTION_KIT_PING).apply {
            setPackage(requireContext().packageName)
            putExtra(EXTRA_PAYLOAD, payload)
        }
        requireContext().sendBroadcast(intent)
        viewModel.onSent(payload)
    }

    companion object {
        const val ACTION_KIT_PING = "com.sys.androidkit.action.KIT_PING"
        private const val EXTRA_PAYLOAD = "payload"
    }
}
