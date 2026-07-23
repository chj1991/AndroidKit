package com.sys.androidkit.feature.network

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sys.androidkit.core.common.result.AppResult
import com.sys.androidkit.core.ui.base.BaseFragment
import com.sys.androidkit.feature.network.databinding.FragmentRetrofitBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RetrofitLabFragment : BaseFragment<FragmentRetrofitBinding>() {

    private val viewModel: RetrofitLabViewModel by viewModels()
    private val adapter = PostsAdapter()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentRetrofitBinding = FragmentRetrofitBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.rvPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPosts.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh(fromSwipe = true) }

        binding.chipList.setOnClickListener { viewModel.setScenario(RetrofitScenario.LIST) }
        binding.chip404.setOnClickListener { viewModel.setScenario(RetrofitScenario.NOT_FOUND) }
        binding.chip500.setOnClickListener { viewModel.setScenario(RetrofitScenario.SERVER_ERROR) }
        binding.chipTimeout.setOnClickListener { viewModel.setScenario(RetrofitScenario.TIMEOUT) }
        binding.btnRun.setOnClickListener { viewModel.refresh(fromSwipe = false) }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.chipList.isChecked = state.scenario == RetrofitScenario.LIST
                    binding.chip404.isChecked = state.scenario == RetrofitScenario.NOT_FOUND
                    binding.chip500.isChecked = state.scenario == RetrofitScenario.SERVER_ERROR
                    binding.chipTimeout.isChecked = state.scenario == RetrofitScenario.TIMEOUT

                    binding.tvMetrics.text = state.metricsText
                    binding.swipeRefresh.isRefreshing =
                        state.isRefreshing || state.result is AppResult.Loading

                    when (val result = state.result) {
                        AppResult.Loading -> {
                            binding.tvStatus.isVisible = true
                            binding.tvStatus.text = getString(R.string.feature_network_loading)
                            adapter.submitList(emptyList())
                        }
                        is AppResult.Success -> {
                            adapter.submitList(result.data)
                            val refreshError = state.refreshError
                            if (refreshError != null) {
                                binding.tvStatus.isVisible = true
                                binding.tvStatus.text =
                                    getString(R.string.feature_network_refresh_error, refreshError)
                            } else {
                                binding.tvStatus.isVisible = false
                            }
                        }
                        is AppResult.Error -> {
                            binding.tvStatus.isVisible = true
                            val type = state.errorType
                            binding.tvStatus.text = if (type != null) {
                                getString(R.string.feature_network_error_typed, type, result.message)
                            } else {
                                getString(R.string.feature_network_error, result.message)
                            }
                            adapter.submitList(emptyList())
                        }
                    }
                }
            }
        }
    }
}
