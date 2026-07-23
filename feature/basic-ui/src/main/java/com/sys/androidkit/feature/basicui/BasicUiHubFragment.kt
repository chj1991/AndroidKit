package com.sys.androidkit.feature.basicui

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
import com.sys.androidkit.feature.basicui.databinding.FragmentBasicUiHubBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BasicUiHubFragment : BaseFragment<FragmentBasicUiHubBinding>() {

    private val viewModel: BasicUiHubViewModel by viewModels()
    private val adapter = BasicUiListAdapter { entry ->
        val request = NavDeepLinkRequest.Builder
            .fromUri("androidkit://demo/basic_ui_item/${entry.id}".toUri())
            .build()
        findNavController().navigate(request)
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentBasicUiHubBinding = FragmentBasicUiHubBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.rvItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvItems.adapter = adapter

        binding.chipAll.setOnClickListener { viewModel.setGroup(null) }
        binding.chipWidget.setOnClickListener { viewModel.setGroup(BasicUiGroup.WIDGET) }
        binding.chipRv.setOnClickListener { viewModel.setGroup(BasicUiGroup.RECYCLER) }
        binding.chipUtils.setOnClickListener { viewModel.setGroup(BasicUiGroup.UTILS) }
        binding.etSearch.doAfterTextChanged { viewModel.setQuery(it?.toString().orEmpty()) }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.chipAll.isChecked = state.group == null
                    binding.chipWidget.isChecked = state.group == BasicUiGroup.WIDGET
                    binding.chipRv.isChecked = state.group == BasicUiGroup.RECYCLER
                    binding.chipUtils.isChecked = state.group == BasicUiGroup.UTILS
                    binding.tvCount.text =
                        getString(R.string.feature_basic_ui_count, state.items.size)
                    adapter.submitList(state.items)
                }
            }
        }
    }
}
