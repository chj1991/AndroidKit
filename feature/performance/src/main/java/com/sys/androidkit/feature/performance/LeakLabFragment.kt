package com.sys.androidkit.feature.performance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.performance.databinding.FragmentLeakBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LeakLabFragment : BaseFragment<FragmentLeakBinding>() {

    private val viewModel: LeakLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentLeakBinding = FragmentLeakBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnLeak.setOnClickListener {
            LeakyActivityHolder.retain(requireActivity())
            viewModel.refresh()
        }
        binding.btnFix.setOnClickListener {
            LeakyActivityHolder.retainApplication(requireContext())
            viewModel.refresh()
        }
        binding.btnClear.setOnClickListener {
            LeakyActivityHolder.clear()
            viewModel.refresh()
        }
        viewModel.refresh()
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    val risk = if (state.leaking) {
                        getString(R.string.feature_performance_leak_risk_yes)
                    } else {
                        getString(R.string.feature_performance_leak_risk_no)
                    }
                    binding.tvStatus.text = "${state.description}\n$risk"
                }
            }
        }
    }
}
