package com.sys.androidkit.feature.performance

import android.os.StrictMode
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.performance.databinding.FragmentStrictModeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StrictModeLabFragment : BaseFragment<FragmentStrictModeBinding>() {

    private val viewModel: StrictModeLabViewModel by viewModels()
    private var updatingUi = false

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentStrictModeBinding = FragmentStrictModeBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.switchStrictMode.setOnCheckedChangeListener { _, isChecked ->
            if (updatingUi) return@setOnCheckedChangeListener
            if (isChecked) enableStrictMode() else disableStrictMode()
            viewModel.setEnabled(isChecked)
        }
        binding.btnDisk.setOnClickListener { triggerDiskWriteOnMain() }
        binding.btnSleep.setOnClickListener { triggerMainThreadSleep() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvStatus.text = state.message
                    updatingUi = true
                    binding.switchStrictMode.isChecked = state.enabled
                    updatingUi = false
                }
            }
        }
    }

    override fun onDestroyView() {
        disableStrictMode()
        super.onDestroyView()
    }

    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .penaltyFlashScreen()
                .build(),
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .build(),
        )
    }

    private fun disableStrictMode() {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX)
        StrictMode.setVmPolicy(StrictMode.VmPolicy.LAX)
    }

    private fun triggerDiskWriteOnMain() {
        val file = File(requireContext().cacheDir, "strictmode-demo.txt")
        file.writeText("strictmode-${System.currentTimeMillis()}")
        viewModel.setMessage("已在主线程写文件：${file.name}（看 Logcat StrictMode）")
    }

    private fun triggerMainThreadSleep() {
        Thread.sleep(300)
        viewModel.setMessage("主线程 sleep 300ms 完成（可能掉帧）")
    }
}
