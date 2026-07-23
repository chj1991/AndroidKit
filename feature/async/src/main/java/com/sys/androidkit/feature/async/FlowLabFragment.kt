package com.sys.androidkit.feature.async

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.async.databinding.FragmentFlowBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FlowLabFragment : BaseFragment<FragmentFlowBinding>() {

    private val viewModel: FlowLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentFlowBinding = FragmentFlowBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnCold.setOnClickListener { viewModel.runColdFlow() }
        binding.btnHot.setOnClickListener { viewModel.runHotShareIn() }
        binding.btnState.setOnClickListener { viewModel.bumpStateFlow() }
        binding.btnShared.setOnClickListener { viewModel.emitSharedEvent() }
        binding.btnReplay.setOnClickListener { viewModel.demoReplaySharedFlow() }
        binding.btnOperators.setOnClickListener { viewModel.runOperators() }
        binding.btnCollectLatest.setOnClickListener { viewModel.runCollectLatest() }
        binding.btnClear.setOnClickListener { viewModel.clearLog() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvMeta.text = getString(
                        R.string.feature_async_flow_meta,
                        state.stateCounter,
                        state.sharedCount,
                    )
                    binding.tvLog.text = state.log
                }
            }
        }
    }
}
