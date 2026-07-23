package com.sys.androidkit.feature.androidktx

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
import com.sys.androidkit.feature.androidktx.databinding.FragmentAndroidKtxHubBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AndroidKtxHubFragment : BaseFragment<FragmentAndroidKtxHubBinding>() {

    private val viewModel: AndroidKtxHubViewModel by viewModels()
    private val adapter = AndroidKtxListAdapter { entry ->
        val request = NavDeepLinkRequest.Builder
            .fromUri("androidkit://demo/android_ktx_item/${entry.id}".toUri())
            .build()
        findNavController().navigate(request)
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentAndroidKtxHubBinding = FragmentAndroidKtxHubBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.rvItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvItems.adapter = adapter

        binding.chipAll.setOnClickListener { viewModel.setGroup(null) }
        binding.chipView.setOnClickListener { viewModel.setGroup(AndroidKtxGroup.VIEW) }
        binding.chipContext.setOnClickListener { viewModel.setGroup(AndroidKtxGroup.CONTEXT) }
        binding.chipActivity.setOnClickListener { viewModel.setGroup(AndroidKtxGroup.ACTIVITY) }
        binding.chipUtil.setOnClickListener { viewModel.setGroup(AndroidKtxGroup.UTIL) }
        binding.etSearch.doAfterTextChanged { viewModel.setQuery(it?.toString().orEmpty()) }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.chipAll.isChecked = state.group == null
                    binding.chipView.isChecked = state.group == AndroidKtxGroup.VIEW
                    binding.chipContext.isChecked = state.group == AndroidKtxGroup.CONTEXT
                    binding.chipActivity.isChecked = state.group == AndroidKtxGroup.ACTIVITY
                    binding.chipUtil.isChecked = state.group == AndroidKtxGroup.UTIL
                    binding.tvCount.text =
                        getString(R.string.feature_android_ktx_count, state.items.size)
                    adapter.submitList(state.items)
                }
            }
        }
    }
}
