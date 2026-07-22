package com.sys.androidkit.feature.performance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.performance.databinding.FragmentStartupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StartupLabFragment : BaseFragment<FragmentStartupBinding>() {

    private val viewModel: StartupLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentStartupBinding = FragmentStartupBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnSerial.setOnClickListener { viewModel.runSerialInit() }
        binding.btnParallel.setOnClickListener { viewModel.runParallelInit() }
        viewModel.refreshAppCost()
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvAppCost.text = if (state.appCreateCostMs >= 0) {
                        getString(R.string.feature_performance_startup_app_cost, state.appCreateCostMs)
                    } else {
                        getString(R.string.feature_performance_startup_app_cost_unknown)
                    }
                    binding.tvSimResult.text = state.simMessage
                    binding.btnSerial.isEnabled = !state.running
                    binding.btnParallel.isEnabled = !state.running
                }
            }
        }
    }
}
