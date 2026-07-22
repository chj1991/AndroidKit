package com.sys.androidkit.feature.async

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.async.databinding.FragmentStructuredConcurrencyBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StructuredConcurrencyLabFragment : BaseFragment<FragmentStructuredConcurrencyBinding>() {

    private val viewModel: StructuredConcurrencyLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentStructuredConcurrencyBinding =
        FragmentStructuredConcurrencyBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnCoroutineScope.setOnClickListener { viewModel.runCoroutineScopeDemo() }
        binding.btnSupervisorScope.setOnClickListener { viewModel.runSupervisorScopeDemo() }
        binding.btnAsync.setOnClickListener { viewModel.runAsyncDemo() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { binding.tvLog.text = it.log }
            }
        }
    }
}
