package com.sys.androidkit.feature.network

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.network.databinding.FragmentProgressBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProgressLabFragment : BaseFragment<FragmentProgressBinding>() {

    private val viewModel: ProgressLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentProgressBinding = FragmentProgressBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnDownload.setOnClickListener { viewModel.download() }
        binding.btnUpload.setOnClickListener { viewModel.upload() }
        binding.btnCancel.setOnClickListener { viewModel.cancel() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvTitle.text = state.title
                    binding.tvDetail.text = state.detail
                    binding.progress.isIndeterminate = state.indeterminate
                    if (!state.indeterminate) {
                        binding.progress.setProgressCompat(state.percent, true)
                    }
                    binding.btnDownload.isEnabled = !state.running
                    binding.btnUpload.isEnabled = !state.running
                    binding.btnCancel.isEnabled = state.running
                }
            }
        }
    }
}
