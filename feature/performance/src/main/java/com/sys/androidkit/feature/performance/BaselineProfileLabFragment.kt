package com.sys.androidkit.feature.performance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.performance.databinding.FragmentBaselineProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BaselineProfileLabFragment : BaseFragment<FragmentBaselineProfileBinding>() {

    private val viewModel: BaselineProfileLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentBaselineProfileBinding =
        FragmentBaselineProfileBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnRefresh.setOnClickListener { viewModel.refresh() }
        binding.btnStartup.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri("androidkit://demo/startup".toUri())
                .build()
            findNavController().navigate(request)
        }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { binding.tvReport.text = it.report }
            }
        }
    }
}
