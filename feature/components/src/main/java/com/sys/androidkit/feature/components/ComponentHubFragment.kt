package com.sys.androidkit.feature.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.components.databinding.FragmentComponentHubBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ComponentHubFragment : BaseFragment<FragmentComponentHubBinding>() {

    private val viewModel: ComponentHubViewModel by viewModels()
    private val adapter = ComponentListAdapter { entry ->
        val request = NavDeepLinkRequest.Builder
            .fromUri("androidkit://demo/component/${entry.id}".toUri())
            .build()
        findNavController().navigate(request)
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentComponentHubBinding = FragmentComponentHubBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.rvComponents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvComponents.adapter = adapter

        binding.chipAll.setOnClickListener { viewModel.setGroup(null) }
        binding.chipSystem.setOnClickListener { viewModel.setGroup(ComponentGroup.SYSTEM) }
        binding.chipM3.setOnClickListener { viewModel.setGroup(ComponentGroup.MATERIAL3) }
        binding.etSearch.doAfterTextChanged { viewModel.setQuery(it?.toString().orEmpty()) }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.chipAll.isChecked = state.group == null
                    binding.chipSystem.isChecked = state.group == ComponentGroup.SYSTEM
                    binding.chipM3.isChecked = state.group == ComponentGroup.MATERIAL3
                    binding.tvCount.text =
                        getString(R.string.feature_components_count, state.items.size)
                    adapter.submitList(state.items)
                }
            }
        }
    }
}
