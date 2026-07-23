package com.sys.androidkit.feature.storage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.storage.databinding.FragmentFilePathBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilePathLabFragment : BaseFragment<FragmentFilePathBinding>() {

    private val viewModel: FilePathLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentFilePathBinding = FragmentFilePathBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnRefresh.setOnClickListener { viewModel.refresh() }
        binding.btnWriteFiles.setOnClickListener { viewModel.writeFilesSample() }
        binding.btnWriteCache.setOnClickListener { viewModel.writeCacheSample() }
        binding.btnClearCache.setOnClickListener { viewModel.clearCacheSample() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvReport.text = state.report
                    binding.tvStatus.text = state.status.ifEmpty {
                        getString(R.string.feature_storage_file_path_status_idle)
                    }
                }
            }
        }
    }
}
