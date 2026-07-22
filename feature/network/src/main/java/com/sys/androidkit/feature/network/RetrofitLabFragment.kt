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
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
    }

    override fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (val result = state.result) {
                        AppResult.Loading -> {
                            binding.swipeRefresh.isRefreshing = true
                            binding.tvStatus.isVisible = true
                            binding.tvStatus.text = "加载中…"
                            adapter.submitList(emptyList())
                        }
                        is AppResult.Success -> {
                            binding.swipeRefresh.isRefreshing = false
                            binding.tvStatus.isVisible = false
                            adapter.submitList(result.data)
                        }
                        is AppResult.Error -> {
                            binding.swipeRefresh.isRefreshing = false
                            binding.tvStatus.isVisible = true
                            binding.tvStatus.text = "错误：${result.message}"
                            adapter.submitList(emptyList())
                        }
                    }
                }
            }
        }
    }
}
