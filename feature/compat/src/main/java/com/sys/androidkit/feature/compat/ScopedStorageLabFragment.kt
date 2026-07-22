package com.sys.androidkit.feature.compat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.compat.databinding.FragmentScopedStorageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScopedStorageLabFragment : BaseFragment<FragmentScopedStorageBinding>() {

    private val viewModel: ScopedStorageLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentScopedStorageBinding =
        FragmentScopedStorageBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnRefresh.setOnClickListener { viewModel.refresh() }
        binding.btnWrite.setOnClickListener { viewModel.writeAppSpecificFile() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvReport.text = state.report
                    binding.tvStatus.text = state.status
                }
            }
        }
    }
}
