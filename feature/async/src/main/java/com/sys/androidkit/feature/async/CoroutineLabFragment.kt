package com.sys.androidkit.feature.async

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.async.databinding.FragmentCoroutineBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoroutineLabFragment : BaseFragment<FragmentCoroutineBinding>() {

    private val viewModel: CoroutineLabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCoroutineBinding = FragmentCoroutineBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnLaunch.setOnClickListener { viewModel.runLaunchCancel() }
        binding.btnCancel.setOnClickListener { viewModel.cancelLongJob() }
        binding.btnDispatchers.setOnClickListener { viewModel.runDispatchers() }
        binding.btnAsync.setOnClickListener { viewModel.runAsyncAwait() }
        binding.btnTimeout.setOnClickListener { viewModel.runTimeout() }
        binding.btnYield.setOnClickListener { viewModel.runYield() }
        binding.btnClear.setOnClickListener { viewModel.clearLog() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { binding.tvLog.text = it.log }
            }
        }
    }
}
