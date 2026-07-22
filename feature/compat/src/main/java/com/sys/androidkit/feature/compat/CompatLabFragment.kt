package com.sys.androidkit.feature.compat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.compat.databinding.FragmentCompatBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompatLabFragment : BaseFragment<FragmentCompatBinding>() {

    private val viewModel: CompatLabViewModel by viewModels()
    private val adapter = CompatChangeAdapter()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentCompatBinding = FragmentCompatBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.rvChanges.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChanges.adapter = adapter
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvDevice.text = getString(
                        R.string.feature_compat_device,
                        state.sdkInt,
                        state.release,
                    )
                    adapter.submitList(state.changes)
                }
            }
        }
    }
}
