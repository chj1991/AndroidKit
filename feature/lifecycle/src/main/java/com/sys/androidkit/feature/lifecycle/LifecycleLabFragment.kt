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
import com.sys.androidkit.feature.lifecycle.databinding.FragmentLifecycleBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LifecycleLabFragment : BaseFragment<FragmentLifecycleBinding>() {

    private val viewModel: LifecycleViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentLifecycleBinding = FragmentLifecycleBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.append("Fragment.onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.append("Fragment.onViewCreated")
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

    override fun onStart() {
        super.onStart()
        viewModel.append("Fragment.onStart")
    }

    override fun onResume() {
        super.onResume()
        viewModel.append("Fragment.onResume")
    }

    override fun onPause() {
        viewModel.append("Fragment.onPause")
        super.onPause()
    }

    override fun onStop() {
        viewModel.append("Fragment.onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        viewModel.append("Fragment.onDestroyView")
        super.onDestroyView()
    }
}
