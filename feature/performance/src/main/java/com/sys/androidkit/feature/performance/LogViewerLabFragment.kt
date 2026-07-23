package com.sys.androidkit.feature.performance

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.performance.databinding.FragmentLogViewerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogViewerLabFragment : BaseFragment<FragmentLogViewerBinding>() {

    private val viewModel: LogViewerLabViewModel by viewModels()
    private val adapter = LogViewerAdapter()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentLogViewerBinding = FragmentLogViewerBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        binding.rvLogs.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true
        }
        binding.rvLogs.adapter = adapter

        binding.chipAll.setOnClickListener { viewModel.setMinPriority(Log.VERBOSE) }
        binding.chipDebug.setOnClickListener { viewModel.setMinPriority(Log.DEBUG) }
        binding.chipInfo.setOnClickListener { viewModel.setMinPriority(Log.INFO) }
        binding.chipWarn.setOnClickListener { viewModel.setMinPriority(Log.WARN) }
        binding.chipError.setOnClickListener { viewModel.setMinPriority(Log.ERROR) }

        binding.btnSample.setOnClickListener { viewModel.emitSamples() }
        binding.btnClear.setOnClickListener { viewModel.clear() }
        binding.btnCopy.setOnClickListener {
            val text = viewModel.exportText()
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("AndroidKit logs", text))
            Toast.makeText(requireContext(), R.string.feature_performance_log_copied, Toast.LENGTH_SHORT).show()
        }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.chipAll.isChecked = state.minPriority == Log.VERBOSE
                    binding.chipDebug.isChecked = state.minPriority == Log.DEBUG
                    binding.chipInfo.isChecked = state.minPriority == Log.INFO
                    binding.chipWarn.isChecked = state.minPriority == Log.WARN
                    binding.chipError.isChecked = state.minPriority == Log.ERROR

                    binding.tvMeta.text = getString(
                        R.string.feature_performance_log_meta,
                        state.logs.size,
                        state.totalCount,
                    )
                    adapter.submitList(state.logs) {
                        if (state.logs.isNotEmpty()) {
                            binding.rvLogs.scrollToPosition(state.logs.lastIndex)
                        }
                    }
                }
            }
        }
    }
}
