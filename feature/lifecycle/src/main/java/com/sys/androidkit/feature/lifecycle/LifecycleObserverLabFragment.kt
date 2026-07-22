package com.sys.androidkit.feature.lifecycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.lifecycle.databinding.FragmentLifecycleObserverBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * LC-04：对比 Fragment 直接覆写回调 vs [DefaultLifecycleObserver] 解耦观察。
 */
@AndroidEntryPoint
class LifecycleObserverLabFragment : BaseFragment<FragmentLifecycleObserverBinding>() {

    private val viewModel: LifecycleObserverLabViewModel by viewModels()

    private val fragmentObserver = LoggingLifecycleObserver("Fragment") { viewModel.append(it) }
    private val viewLifecycleObserver = LoggingLifecycleObserver("ViewLifecycle") {
        viewModel.append(it)
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentLifecycleObserverBinding =
        FragmentLifecycleObserverBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(fragmentObserver)
        viewModel.append("Fragment.onCreate → addObserver(Fragment)")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(viewLifecycleObserver)
        viewModel.append("onViewCreated → addObserver(ViewLifecycle)")
    }

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.btnClear.setOnClickListener { viewModel.clear() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvLogs.text = state.logs.joinToString("\n")
                }
            }
        }
    }

    override fun onDestroyView() {
        viewLifecycleOwner.lifecycle.removeObserver(viewLifecycleObserver)
        viewModel.append("onDestroyView → removeObserver(ViewLifecycle)")
        super.onDestroyView()
    }

    override fun onDestroy() {
        lifecycle.removeObserver(fragmentObserver)
        viewModel.append("onDestroy → removeObserver(Fragment)")
        super.onDestroy()
    }
}
